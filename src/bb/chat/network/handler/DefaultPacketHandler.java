package bb.chat.network.handler;

import bb.chat.enums.QuerryType;
import bb.chat.enums.ServerStatus;
import bb.chat.enums.Side;
import bb.chat.interfaces.APacket;
import bb.chat.interfaces.IConnectionHandler;
import bb.chat.interfaces.IIOHandler;
import bb.chat.network.packet.Chatting.ChatPacket;
import bb.chat.network.packet.Command.*;
import bb.chat.network.packet.Handshake.LoginPacket;
import bb.chat.network.packet.Handshake.SignUpPacket;
import bb.chat.security.BasicUser;
import bb.chat.security.BasicUserDatabase;
import bb.util.file.database.FileWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by BB20101997 on 05.09.2014.
 */
public final class DefaultPacketHandler extends BasicPacketHandler<APacket> {

	@SuppressWarnings("unchecked")
	public DefaultPacketHandler(IConnectionHandler imh) {
		super(imh);
		addAssociatedPacket(ChatPacket.class);
		addAssociatedPacket(DisconnectPacket.class);
		addAssociatedPacket(RenamePacket.class);
		addAssociatedPacket(StopPacket.class);
		addAssociatedPacket(WhisperPacket.class);
		addAssociatedPacket(LoginPacket.class);
		addAssociatedPacket(SignUpPacket.class);
		addAssociatedPacket(SavePacket.class);
		addAssociatedPacket(PermissionPacket.class);
		addAssociatedPacket(QuerryPacket.class);
	}

	@SuppressWarnings("UnusedParameters")
	void handlePacket(ChatPacket cp, IIOHandler sender) {
		if(IMH.getSide() == Side.CLIENT) {
			IMH.println("[" + cp.Sender + "] " + cp.message);
		} else {
			IMH.println("[" + cp.Sender + "] " + cp.message);
			IMH.sendPackage(cp, IConnectionHandler.ALL);
		}
	}

	@SuppressWarnings("UnusedParameters")
	void handlePacket(RenamePacket rp, IIOHandler sender) {
		if(IMH.getSide() == Side.CLIENT) {
			if(IMH.getActor().getActorName().equals(rp.oldName)) {
				IMH.getActor().setActorName(rp.newName);
			}
		} else {
			IIOHandler io;
			if((io = IMH.getConnectionByName(rp.oldName)) != null && io.setActorName(rp.newName)) {
				IMH.sendPackage(new ChatPacket(rp.oldName + " is now known as " + rp.newName, IMH.getActor().getActorName()), IConnectionHandler.ALL);
				IMH.sendPackage(new RenamePacket(rp.oldName, rp.newName), io);
				IMH.println("[Server] : " + rp.oldName + " is now known as " + rp.newName);
			} else {
				IMH.sendPackage(new ChatPacket("Couldn't rename User!", IMH.getActor().getActorName()), sender);
			}
		}
	}

	@SuppressWarnings("UnusedParameters")
	void handlePacket(DisconnectPacket dp, IIOHandler sender) {
		IMH.disconnect(sender);
		if(IMH.getSide() == Side.SERVER) {
			IMH.sendPackage(new ChatPacket(sender.getActorName() + " disconnected!", IMH.getActor().getActorName()), IConnectionHandler.ALL);
		}
		IMH.println("[" + IMH.getActor().getActorName() + "] " + sender.getActorName() + " disconnected!");
	}

	@SuppressWarnings("UnusedParameters")
	void handlePacket(StopPacket sp, IIOHandler sender) {
		if(IMH.getSide() == Side.SERVER) {
			IMH.disconnect(IConnectionHandler.ALL);
			IMH.getIChatInstance().shutdown();
		}
	}

	@SuppressWarnings("UnusedParameters")
	void handlePacket(WhisperPacket wp, IIOHandler sender) {
		if(IMH.getSide() == Side.SERVER && !wp.getReceiver().equals(IMH.getActor().getActorName())) {
			IMH.sendPackage(wp, IMH.getConnectionByName(wp.getReceiver()));
		} else {
			if(IMH.getActor().getActorName().equals(wp.getReceiver())) {
				IMH.println("[" + wp.getSender() + " whispered to you: ] \"" + wp.getMessage() + "\"");
			}
		}
	}

	@SuppressWarnings("UnusedParameters")
	void handlePacket(SavePacket sp, IIOHandler sender) {
		if(IMH.getSide() == Side.SERVER) {
			IMH.getIChatInstance().save();
		}
	}

	@SuppressWarnings("unchecked")
	void handlePacket(LoginPacket lp, IIOHandler sender) {

		BasicUser u = IMH.getIChatInstance().getUserDatabase().getUserByName(lp.getUsername());
		if(u == null) {
			System.out.println("User doesn't exist!");
		}
		if(u != null && u.checkPassword(lp.getPassword())) {
			sender.setUser(u);
		} else {
			IMH.sendPackage(new ChatPacket("Your login has failed either the password or username was wrong, please try again!", IConnectionHandler.SERVER.getActorName()), sender);
		}

	}

	void handlePacket(PermissionPacket pp, IIOHandler sender) {
		IMH.getIChatInstance().getPermissionRegistry().executePermissionCommand(IMH, sender, pp.cmd, pp.restCmd);

	}

	void handlePacket(SignUpPacket sup, IIOHandler sender) {
		if(IMH.getSide() == Side.SERVER) {
			BasicUserDatabase bud = IMH.getIChatInstance().getUserDatabase();
			if(bud.createAndAddNewUser(sup.getUsername(), sup.getPassword()) != null) {
				IMH.println("[SERVER] : An Account with username " + sup.getUsername() + " was created!");
				IMH.sendPackage(new ChatPacket("User-Account successfully created!", IMH.getActor().getActorName()), sender);
			} else {
				IMH.println("[SERVER] : Tried to create an Account with username " + sup.getUsername() + " already existed!");
				IMH.sendPackage(new ChatPacket("Could not create User-Account,already existed!", IMH.getActor().getActorName()), sender);
			}
		}
	}

	void handlePacket(QuerryPacket qp, IIOHandler sender) {
		boolean server = IMH.getSide() == Side.SERVER;
		boolean request = qp.isRequest();
		QuerryType querryType = qp.getQT();
		if(request && server) {
			QuerryPacket querryPacket = new QuerryPacket();

			switch(querryType) {

				case SERVERNAME: {
					querryPacket = new QuerryPacket(querryType, IMH.getServerName());
					break;
				}
				case SERVERSTATUS: {
					querryPacket = new QuerryPacket(querryType, String.valueOf(IMH.getServerStatus().ordinal()));
					break;
				}
				case SERVERMESSAGE: {
					querryPacket = new QuerryPacket(querryType, IMH.getServerMessage());
					break;
				}
				case ONLINEUSERSNUMBER: {
					querryPacket = new QuerryPacket(querryType, String.valueOf(IMH.getOnlineUsers()));
					break;
				}
				case ONLINEUSERSLIST: {
					FileWriter fw = new FileWriter();
					String[] names = IMH.getActiveUserList();
					fw.add(names.length, "SIZE");
					for(int i = 0; i < names.length; i++) {
						fw.add(names[i], "NR" + i);
					}
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					try {
						fw.writeToStream(baos);
						querryPacket = new QuerryPacket(querryType, baos.toString());
					} catch(IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case MAXONLINEUSERS: {
					querryPacket = new QuerryPacket(querryType, String.valueOf(IMH.getMaxUsers()));
					break;
				}
			}
			IMH.sendPackage(querryPacket, sender);
		}
		if(!(request | server)) {
			switch(querryType) {

				case SERVERNAME:
					IMH.setServerName(qp.getResponse());
					break;
				case SERVERSTATUS:
					IMH.setServerStatus(ServerStatus.values()[Integer.valueOf(qp.getResponse())]);
					break;
				case SERVERMESSAGE:
					IMH.setServerMessage(qp.getResponse());
					break;
				case ONLINEUSERSNUMBER:
					IMH.setOnlineUsers(Integer.valueOf(qp.getResponse()));
					break;
				case ONLINEUSERSLIST:
					try {
						FileWriter fw = new FileWriter();
						ByteArrayInputStream bais = new ByteArrayInputStream(qp.getResponse().getBytes());
						fw.readFromStream(bais);
						int size = (int) fw.get("SIZE");
						String[] names = new String[size];
						for(int i = 0; i < size; i++) {
							names[i] = (String) fw.get("NR" + i);
						}
						IMH.setActiveUsers(names);
					} catch(IOException e) {
						e.printStackTrace();
					}
					break;
				case MAXONLINEUSERS:
					IMH.setMaxUsers(Integer.valueOf(qp.getResponse()));
					break;
			}
		}
	}

}
