package bb.chat.network.handler;

import bb.chat.enums.QuerryType;
import bb.chat.enums.Side;
import bb.chat.interfaces.IIOHandler;
import bb.chat.interfaces.IMessageHandler;
import bb.chat.interfaces.IPacket;
import bb.chat.interfaces.IServerMessageHandler;
import bb.chat.network.packet.Chatting.ChatPacket;
import bb.chat.network.packet.Command.*;
import bb.chat.network.packet.Handshake.LoginPacket;
import bb.chat.network.packet.Handshake.SignUpPacket;
import bb.chat.security.BasicUser;
import bb.chat.security.BasicUserDatabase;
import bb.util.file.database.FileWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by BB20101997 on 05.09.2014.
 */
public final class DefaultPacketHandler extends BasicPacketHandler<IPacket> {

	@SuppressWarnings("unchecked")
	public DefaultPacketHandler(IMessageHandler imh) {
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

	@Override
	@SuppressWarnings("unchecked")
	public void HandlePacket(IPacket iPacket, IIOHandler sender) {

		int id = IMH.getPacketRegistrie().getID(iPacket.getClass());

		System.out.println("DefaultPacketHandler : " + iPacket.getClass() + ", ID : " + id);

		try {
			Method m = getClass().getDeclaredMethod("handlePacket", iPacket.getClass(), IIOHandler.class);
			m.invoke(this, iPacket, sender);
		} catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		}

	}


	@SuppressWarnings("UnusedParameters")
	private void handlePacket(ChatPacket cp, IIOHandler sender) {
		if(IMH.getSide() == Side.CLIENT) {
			IMH.println("[" + cp.Sender + "] " + cp.message);
		} else {
			IMH.println("[" + cp.Sender + "] " + cp.message);
			IMH.sendPackage(cp, IMessageHandler.ALL);
		}
	}

	@SuppressWarnings("UnusedParameters")
	private void handlePacket(RenamePacket rp, IIOHandler sender) {
		if(IMH.getSide() == Side.CLIENT) {
			if(IMH.getActor().getActorName().equals(rp.oldName)) {
				IMH.getActor().setActorName(rp.newName);
			}
		} else {
			IIOHandler io;
			if((io = IMH.getUserByName(rp.oldName)) != null && io.setActorName(rp.newName)) {
				IMH.sendPackage(new ChatPacket(rp.oldName + " is now known as " + rp.newName, IMH.getActor().getActorName()), IMessageHandler.ALL);
				IMH.sendPackage(new RenamePacket(rp.oldName, rp.newName), io);
				IMH.println("[Server] : " + rp.oldName + " is now known as " + rp.newName);
			} else {
				IMH.sendPackage(new ChatPacket("Couldn't rename User!", IMH.getActor().getActorName()), sender);
			}
		}
	}

	@SuppressWarnings("UnusedParameters")
	private void handlePacket(DisconnectPacket dp, IIOHandler sender) {
		IMH.disconnect(sender);
		IMH.sendPackage(new ChatPacket(sender.getActorName() + " disconnected!", IMH.getActor().getActorName()), IMessageHandler.ALL);
		IMH.println("[" + IMH.getActor().getActorName() + "] " + sender.getActorName() + " disconnected!");
	}

	@SuppressWarnings("UnusedParameters")
	private void handlePacket(StopPacket sp, IIOHandler sender) {
		if(IMH.getSide() == Side.SERVER) {
			IMH.disconnect(IMessageHandler.ALL);
			IMH.shutdown();
		}
	}

	@SuppressWarnings("UnusedParameters")
	private void handlePacket(WhisperPacket wp, IIOHandler sender) {
		if(IMH.getSide() == Side.SERVER && !wp.getReceiver().equals(IMH.getActor().getActorName())) {
			IMH.sendPackage(wp, IMH.getUserByName(wp.getReceiver()));
		} else {
			if(IMH.getActor().getActorName().equals(wp.getReceiver())) {
				IMH.println("[" + wp.getSender() + " whispered to you: ] \"" + wp.getMessage() + "\"");
			}
		}
	}

	@SuppressWarnings("UnusedParameters")
	private void handlePacket(SavePacket sp, IIOHandler sender) {
		if(IMH.getSide() == Side.SERVER) {
			IMH.save();
		}
	}

	@SuppressWarnings("unchecked")
	private void handlePacket(LoginPacket lp, IIOHandler sender) {

		BasicUser u = IMH.getUserDatabase().getUserByName(lp.getUsername());
		if(u == null) {
			System.out.println("User doesn't exist!");
		}
		if(u != null && u.checkPassword(lp.getPassword())) {
			sender.setUser(u);
		} else {
			IMH.sendPackage(new ChatPacket("Your login has failed either the password or username was wrong, please try again!", IMessageHandler.SERVER.getActorName()), sender);
		}

	}

	private void handlePacket(PermissionPacket pp, IIOHandler sender) {
		IMH.getPermissionRegistry().executePermissionCommand(IMH, sender, pp.cmd, pp.restCmd);

	}

	private void handlePacket(SignUpPacket sup, IIOHandler sender) {
		if(IMH.getSide() == Side.SERVER) {
			BasicUserDatabase bud = IMH.getUserDatabase();
			if(bud.createAndAddNewUser(sup.getUsername(), sup.getPassword()) != null) {
				IMH.println("[SERVER] : An Account with username " + sup.getUsername() + " was created!");
				IMH.sendPackage(new ChatPacket("User-Account successfully created!", IMH.getActor().getActorName()), sender);
			} else {
				IMH.println("[SERVER] : Tried to create an Account with username " + sup.getUsername() + " already existed!");
				IMH.sendPackage(new ChatPacket("Could not create User-Account,already existed!", IMH.getActor().getActorName()), sender);
			}
		}
	}

	private void handlePacket(QuerryPacket qp, IIOHandler sender) {
		boolean server = IMH.getSide() == Side.SERVER;
		boolean request = qp.isRequest();
		QuerryType querryType = qp.getQT();
		if(request && server) {
			QuerryPacket querryPacket = new QuerryPacket();

			switch(querryType) {

				case SERVERNAME: {
					if(IMH instanceof IServerMessageHandler) {
						querryPacket = new QuerryPacket(querryType, ((IServerMessageHandler) IMH).getServerName());
					}
					break;
				}

				case SERVERSTATUS: {
					if(IMH instanceof IServerMessageHandler) {
						querryPacket = new QuerryPacket(querryType,String.valueOf(((IServerMessageHandler) IMH).getServerStatus().ordinal()));
					}
					break;
				}
				case SERVERMESSAGE: {
					if(IMH instanceof IServerMessageHandler) {
						querryPacket = new QuerryPacket(querryType, ((IServerMessageHandler) IMH).getServerMessage());
					}
					break;
				}
				case ONLINEUSERSNUMBER: {
					if(IMH instanceof IServerMessageHandler) {
						int n = ((IServerMessageHandler) IMH).getOnlineUsers();
						querryPacket = new QuerryPacket(querryType, String.valueOf(n));
					}
					break;
				}
				case ONLINEUSERSLIST: {
					if(IMH instanceof IServerMessageHandler) {
						FileWriter fw = new FileWriter();
						String[] names = ((IServerMessageHandler) IMH).getActiveUserList();
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
					}
					break;
				}
				case MAXONLINEUSERS: {

					if(IMH instanceof IServerMessageHandler) {
						querryPacket = new QuerryPacket(querryType, String.valueOf(((IServerMessageHandler) IMH).getMaxUsers()));
					}
				}
			}
			IMH.sendPackage(querryPacket, sender);
		}
		if(!(request|server)){
			switch(querryType){

				case SERVERNAME:
					break;//TODO
				case SERVERSTATUS:
					break;//TODO
				case SERVERMESSAGE:
					break;//TODO
				case ONLINEUSERSNUMBER:
					break;//TODO
				case ONLINEUSERSLIST:
					break;//TODO
				case MAXONLINEUSERS:
					break;//TODO
			}
		}
	}

}
