package bb.chat.network.handler;

import bb.chat.chat.ChatActor;
import bb.chat.enums.QuerryType;
import bb.chat.interfaces.IChat;
import bb.chat.network.packet.chatting.ChatPacket;
import bb.chat.network.packet.command.*;
import bb.chat.network.packet.handshake.LoginPacket;
import bb.chat.network.packet.handshake.SignUpPacket;
import bb.chat.security.BasicUser;
import bb.chat.security.BasicUserDatabase;
import bb.net.enums.ServerStatus;
import bb.net.enums.Side;
import bb.net.handler.BasicPacketHandler;
import bb.net.interfaces.IIOHandler;
import bb.net.packets.connecting.DisconnectPacket;
import bb.util.file.database.FileWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Created by BB20101997 on 05.09.2014.
 */

public final class DefaultPacketHandler extends BasicPacketHandler {

	IChat ICHAT;

	@SuppressWarnings("unchecked")
	public DefaultPacketHandler(IChat ic) {
		super(ic.getIConnectionManager().getPacketRegistrie());
		ICHAT = ic;

		addAssociatedPacket(ChatPacket.class);
		addAssociatedPacket(RenamePacket.class);
		addAssociatedPacket(DisconnectPacket.class);
		addAssociatedPacket(StopPacket.class);
		addAssociatedPacket(WhisperPacket.class);
		addAssociatedPacket(SavePacket.class);
		addAssociatedPacket(LoginPacket.class);
		addAssociatedPacket(PermissionPacket.class);
		addAssociatedPacket(SignUpPacket.class);
		addAssociatedPacket(QuerryPacket.class);
	}

	@SuppressWarnings("UnusedParameters")
	public void handlePacket(ChatPacket cp, IIOHandler sender) {

		if(ICHAT.getIConnectionManager().getSide() == Side.SERVER) {
			if(sender != ICHAT.getIConnectionManager().ALL() || sender != ICHAT.getIConnectionManager().SERVER()) {
				ChatActor ca = ICHAT.getActorByIIOHandler(sender);
				if(ca != null) {
					if(!Objects.equals(cp.Sender, ca.getActorName())) {
						sender.sendPacket(new RenamePacket("Client", ca.getActorName()));
					}
					cp.Sender = ca.getActorName();
				}
				if(sender != ICHAT.getIConnectionManager().LOCAL()) {
					System.out.println("Sending CP to ALL");
					ICHAT.getIConnectionManager().sendPackage(cp, ICHAT.getIConnectionManager().ALL());
				}
			}
		}
		ICHAT.getBasicChatPanel().println("[" + cp.Sender + "] " + cp.Message);

	}

	@SuppressWarnings("UnusedParameters")
	public void handlePacket(RenamePacket rp, IIOHandler sender) {
		if(ICHAT.getIConnectionManager().getSide() == Side.CLIENT) {
			if(ICHAT.getLocalActor().getActorName().equals(rp.oldName)) {
				ICHAT.getLocalActor().setActorName(rp.newName);
				ICHAT.getBasicChatPanel().println("[SERVER]:You are now known as " + rp.newName + "!");
			} else {
				ICHAT.getBasicChatPanel().println("[SERVER]:user:" + rp.oldName + " is now known as " + rp.newName + "!");
			}

		} else {
			ChatActor io;
			if((io = ICHAT.getActorByName(rp.oldName)) != null && io.setActorName(rp.newName)) {
				handlePacket(new ChatPacket(rp.oldName + " is now known as " + rp.newName, ICHAT.getLocalActor().getActorName()), ICHAT.getIConnectionManager().ALL());
				ICHAT.getIConnectionManager().sendPackage(new RenamePacket(rp.oldName, rp.newName), io.getIIOHandler());
			} else {
				ICHAT.getIConnectionManager().sendPackage(new ChatPacket("Couldn't rename user!", ICHAT.getLocalActor().getActorName()), sender);
			}
		}
	}

	@SuppressWarnings("UnusedParameters")
	public void handlePacket(DisconnectPacket dp, IIOHandler sender) {
		if(ICHAT.getIConnectionManager().getSide() == Side.SERVER) {
			ChatActor ca = ICHAT.getActorByIIOHandler(sender);
			if(ca != null) {
				handlePacket(new ChatPacket(ca.getActorName() + " disconnected!", "LOGOUT"), ICHAT.getIConnectionManager().ALL());
			} else {
				handlePacket(new ChatPacket("Unknown disconnected!", "LOGOUT"), ICHAT.getIConnectionManager().ALL());
			}
		}
	}

	@SuppressWarnings("UnusedParameters")
	public void handlePacket(StopPacket sp, IIOHandler sender) {
		if(ICHAT.getIConnectionManager().getSide() == Side.SERVER) {
			ICHAT.getIConnectionManager().disconnect(ICHAT.getIConnectionManager().ALL());
			ICHAT.shutdown();
		}
	}

	@SuppressWarnings("UnusedParameters")
	public void handlePacket(WhisperPacket wp, IIOHandler sender) {
		if(ICHAT.getIConnectionManager().getSide() == Side.SERVER && !wp.getReceiver().equals(ICHAT.getLocalActor().getActorName())) {
			ICHAT.getIConnectionManager().sendPackage(wp, ICHAT.getActorByName(wp.getReceiver()).getIIOHandler());
		} else {
			if(ICHAT.getLocalActor().getActorName().equals(wp.getReceiver())) {
				ICHAT.getBasicChatPanel().println("[" + wp.getSender() + " whispered to you: ] \"" + wp.getMessage() + "\"");
			}
		}
	}

	@SuppressWarnings("UnusedParameters")
	public void handlePacket(SavePacket sp, IIOHandler sender) {
		if(ICHAT.getIConnectionManager().getSide() == Side.SERVER) {
			ICHAT.save();
		}
	}

	@SuppressWarnings("unchecked")
	public void handlePacket(LoginPacket lp, IIOHandler sender) {

		ChatActor ca = ICHAT.getActorByIIOHandler(sender);

		BasicUser u = ICHAT.getUserDatabase().getUserByName(lp.getUsername());
		if(u == null) {

		} else if(u.checkPassword(lp.getPassword())) {
			String nameOld = ca.getActorName();
			ca.setUser(u);
			String nameNew = ca.getActorName();
			ICHAT.getIConnectionManager().sendPackage(new RenamePacket(nameOld, nameNew), sender);
			ICHAT.getIConnectionManager().sendPackage(new ChatPacket("You have successfully logged in as " + lp.getUsername(), "LOGIN"), sender);
			handlePacket(new ChatPacket(ca.getActorName() + " joined the Chat!", ICHAT.getLocalActor().getActorName()), ICHAT.getIConnectionManager().ALL());

		} else {
			ICHAT.getIConnectionManager().sendPackage(new ChatPacket("Your login has failed the password or username(maybe both?) was wrong, please try again!", ICHAT.getLocalActor().getActorName()), sender);
		}

	}

	public void handlePacket(PermissionPacket pp, IIOHandler sender) {
		ICHAT.getPermissionRegistry().executePermissionCommand(ICHAT, sender, pp.cmd, pp.restCmd);
	}

	public void handlePacket(SignUpPacket sup, IIOHandler sender) {
		if(ICHAT.getIConnectionManager().getSide() == Side.SERVER) {
			BasicUserDatabase bud = ICHAT.getUserDatabase();
			if(bud.createAndAddNewUser(sup.getUsername(), sup.getPassword()) != null) {
				ICHAT.getBasicChatPanel().println("[SERVER] : An Account with username " + sup.getUsername() + " was created!");
				ICHAT.getIConnectionManager().sendPackage(new ChatPacket("user-Account successfully created!", ICHAT.getLocalActor().getActorName()), sender);
			} else {
				ICHAT.getBasicChatPanel().println("[SERVER] : Tried to create an Account with username " + sup.getUsername() + " already existed!");
				ICHAT.getIConnectionManager().sendPackage(new ChatPacket("Could not create user-Account,already existed!", ICHAT.getLocalActor().getActorName()), sender);
			}
		}
	}

	@SuppressWarnings("StringConcatenationMissingWhitespace")
	public void handlePacket(QuerryPacket qp, IIOHandler sender) {
		boolean server = ICHAT.getIConnectionManager().getSide() == Side.SERVER;
		boolean request = qp.isRequest();
		QuerryType querryType = qp.getQT();
		if(request && server) {
			QuerryPacket querryPacket = new QuerryPacket();

			switch(querryType) {

				case SERVERNAME: {
					querryPacket = new QuerryPacket(querryType, ICHAT.getServerName());
					break;
				}
				case SERVERSTATUS: {
					querryPacket = new QuerryPacket(querryType, String.valueOf(ICHAT.getIConnectionManager().getServerStatus().ordinal()));
					break;
				}
				case SERVERMESSAGE: {
					querryPacket = new QuerryPacket(querryType, ICHAT.getServerMessage());
					break;
				}
				case ONLINEUSERSNUMBER: {
					querryPacket = new QuerryPacket(querryType, String.valueOf(ICHAT.getOnlineUsers()));
					break;
				}
				case ONLINEUSERSLIST: {
					FileWriter fw = new FileWriter();
					String[] names = ICHAT.getActiveUserList();
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
					querryPacket = new QuerryPacket(querryType, String.valueOf(ICHAT.getMaxUsers()));
					break;
				}
			}
			ICHAT.getIConnectionManager().sendPackage(querryPacket, sender);
		}
		if(!(request | server)) {
			switch(querryType) {

				case SERVERNAME:
					ICHAT.setServerName(qp.getResponse());
					break;
				case SERVERSTATUS:
					ICHAT.getIConnectionManager().setServerStatus(ServerStatus.values()[Integer.valueOf(qp.getResponse())]);
					break;
				case SERVERMESSAGE:
					ICHAT.setServerMessage(qp.getResponse());
					break;
				case ONLINEUSERSNUMBER:
					ICHAT.setOnlineUsers(Integer.valueOf(qp.getResponse()));
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
						ICHAT.setActiveUsers(names);
					} catch(IOException e) {
						e.printStackTrace();
					}
					break;
				case MAXONLINEUSERS:
					ICHAT.setMaxUsers(Integer.valueOf(qp.getResponse()));
					break;
			}
		}
	}

}