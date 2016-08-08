package bb.chat.network.handler;

import bb.chat.enums.Bundles;
import bb.chat.enums.QuerryType;
import bb.chat.interfaces.IBasicChatPanel;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.IChatActor;
import bb.chat.network.packet.chatting.ChatPacket;
import bb.chat.network.packet.chatting.MessagePacket;
import bb.chat.network.packet.command.*;
import bb.chat.network.packet.handshake.LoginPacket;
import bb.chat.network.packet.handshake.SignUpPacket;
import bb.chat.security.BasicUser;
import bb.chat.security.BasicUserDatabase;
import bb.net.enums.ServerStatus;
import bb.net.enums.Side;
import bb.net.handler.BasicPacketHandler;
import bb.net.interfaces.IConnectionManager;
import bb.net.interfaces.IIOHandler;
import bb.util.file.database.FileWriter;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Created by BB20101997 on 05.09.2014.
 */
//the default packet handler
public final class DefaultPacketHandler extends BasicPacketHandler {

	private final IChat              ICHAT;
	private final IConnectionManager ICM;

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(DefaultPacketHandler.class.getName());
		//noinspection DuplicateStringLiteralInspection
		log.addHandler(new BBLogHandler(Constants.getLogFile("ChatBasis")));
	}



	@SuppressWarnings("unused")
	public DefaultPacketHandler(IChat ic) {
		super(ic.getIConnectionManager().getPacketRegistrie());
		ICHAT = ic;
		ICM = ICHAT.getIConnectionManager();

		addAssociatedPacket(ChatPacket.class);
		addAssociatedPacket(RenamePacket.class);
		//addAssociatedPacket(DisconnectPacket.class);
		addAssociatedPacket(StopPacket.class);
		addAssociatedPacket(WhisperPacket.class);
		addAssociatedPacket(SavePacket.class);
		addAssociatedPacket(LoginPacket.class);
		addAssociatedPacket(PermissionPacket.class);
		addAssociatedPacket(SignUpPacket.class);
		addAssociatedPacket(QuerryPacket.class);
		addAssociatedPacket(MessagePacket.class);
	}

	public void handlePacket(ChatPacket cp, IIOHandler sender) {
		log.finer("Handling ChatPacket on side "+ICM.getSide()+" with the Message:"+cp.Message);
		if(ICM.getSide() == Side.SERVER) {
			if(sender != ICM.ALL() || sender != ICM.SERVER()) {
				IChatActor ca = ICHAT.getActorByIIOHandler(sender);
				if(ca != null) {
					if(!Objects.equals(cp.Sender, ca.getActorName())) {
						log.warning("Sender not as expected received:"+cp.Sender+" Expected:"+ca.getActorName());
						sender.sendPacket(new RenamePacket(cp.Sender,ca.getActorName()));
					}
					cp.Sender = ca.getActorName();
				}
				if(sender != ICHAT.getIConnectionManager().LOCAL()) {
					log.fine("Sending CP to ALL");
					ICM.sendPackage(cp, ICM.ALL());
				}
			}
		}
		ICHAT.getBasicChatPanel().println(MessageFormat.format(Bundles.MESSAGE.getResource().getString("msg"),cp.Sender,cp.Message));

	}

	@SuppressWarnings("UnusedParameters")
	public void handlePacket(MessagePacket mp, IIOHandler sender){
		ICHAT.getBasicChatPanel().println(MessageFormat.format(Bundles.MESSAGE.getResource().getString(mp.stringKey), (Object[]) mp.stringArgs));
	}

	@SuppressWarnings("unused")
	public void handlePacket(RenamePacket rp, IIOHandler sender) {
		final IBasicChatPanel BCP = ICHAT.getBasicChatPanel();
		log.finer("Handling RenamePacket");
		if(ICM.getSide() == Side.CLIENT) {
			if(ICHAT.getLOCALActor().getActorName().equals(rp.oldName)) {
				log.finer("RenamePacket renames Me");
				ICHAT.getLOCALActor().setActorName(rp.newName,false);
				BCP.println(MessageFormat.format(Bundles.MESSAGE.getResource().getString("rename.success"),"SERVER",rp.newName));
			} else {
				if("Client".equals(rp.oldName)) {
					log.finer("RenamePacket is a join.");
					BCP.println(MessageFormat.format(Bundles.MESSAGE.getResource().getString("rename.join"),"SERVER",rp.newName));
				} else {
					log.finer("RenamePacket is Renaming "+rp.oldName+" to "+rp.newName+".");
					BCP.println(MessageFormat.format(Bundles.MESSAGE.getResource().getString("rename.announce"),"SERVER",rp.oldName,rp.newName));
				}
			}
		} else {
			IChatActor io = ICHAT.getActorByName(rp.oldName);
			if(io != null && io.setActorName(rp.newName,true)) {
				BCP.println(MessageFormat.format(Bundles.MESSAGE.getResource().getString("rename.announce"),ICHAT.getLOCALActor().getActorName(), rp.oldName, rp.newName));
			} else {
				ICM.sendPackage(new MessagePacket("rename.failed"),sender);
			}
		}
	}

	@SuppressWarnings({"UnusedParameters", "unused"})
	public void handlePacket(StopPacket sp, IIOHandler sender) {
		if(ICHAT.getIConnectionManager().getSide() == Side.SERVER) {
			ICHAT.getIConnectionManager().disconnect(ICHAT.getIConnectionManager().ALL());
			ICHAT.shutdown();
		}
	}

	@SuppressWarnings({"UnusedParameters", "unused"})
	public void handlePacket(WhisperPacket wp, IIOHandler sender) {
		if(ICM.getSide() == Side.SERVER && !wp.getReceiver().equals(ICHAT.getLOCALActor().getActorName())) {
			ICM.sendPackage(wp, ICHAT.getActorByName(wp.getReceiver()).getIIOHandler());
		} else {
			if(ICHAT.getLOCALActor().getActorName().equals(wp.getReceiver())) {
				ICHAT.getBasicChatPanel().println(MessageFormat.format(Bundles.MESSAGE.getResource().getString("whisper.receiver"),wp.getSender(),wp.getMessage()));
			}
		}
	}

	@SuppressWarnings("unused")
	public void handlePacket(SavePacket sp, IIOHandler sender) {
		if(ICM.getSide() == Side.SERVER) {
			ICHAT.save();
			ICM.sendPackage(new MessagePacket("save.success"),sender);
		}
	}

	@SuppressWarnings("unused")
	public void handlePacket(LoginPacket lp, IIOHandler sender) {

		IChatActor ca = ICHAT.getActorByIIOHandler(sender);

		BasicUser u = ICHAT.getUserDatabase().getUserByName(lp.getUsername());
		if(u != null) {
			if(u.checkPassword(lp.getPassword())) {
				String nameOld = ca.getActorName();
				ca.setUser(u);
				String nameNew = ca.getActorName();
				ICM.sendPackage(new RenamePacket(nameOld, nameNew), sender);
				ICM.sendPackage(new MessagePacket("join.success",lp.getUsername()),sender);
				handlePacket(new MessagePacket("rename.join",ICHAT.getLOCALActor().getActorName(),ca.getActorName()), ICM.ALL());
			} else {
				ICM.sendPackage(new MessagePacket("login.fail"),sender);
			}
		}

	}

	@SuppressWarnings("unused")
	public void handlePacket(PermissionPacket pp, IIOHandler sender) {
		ICHAT.getPermissionRegistry().executePermissionCommand(ICHAT, sender, pp.getCommand());
	}

	@SuppressWarnings("unused")
	public void handlePacket(SignUpPacket sup, IIOHandler sender) {
		final IBasicChatPanel BCP = ICHAT.getBasicChatPanel();
		if(ICM.getSide() == Side.SERVER) {
			BasicUserDatabase bud = ICHAT.getUserDatabase();
			if(bud.createAndAddNewUser(sup.getUsername(), sup.getPassword()) != null) {
				BCP.println(MessageFormat.format(Bundles.MESSAGE.getResource().getString("register.success.server"),sup.getUsername()));
				ICM.sendPackage(new MessagePacket("register.success.client"),sender);
			} else {
				BCP.println(MessageFormat.format(Bundles.MESSAGE.getResource().getString("register.fail.server"),sup.getUsername()));
				ICM.sendPackage(new MessagePacket("register.fail.client"),sender);
			}
		}
	}

	@SuppressWarnings({"StringConcatenationMissingWhitespace", "unused"})
	public void handlePacket(QuerryPacket qp, IIOHandler sender) {
		boolean server = ICM.getSide() == Side.SERVER;
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
					querryPacket = new QuerryPacket(querryType, String.valueOf(ICM.getServerStatus().ordinal()));
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
			ICM.sendPackage(querryPacket, sender);
		}
		if(!(request | server)) {
			switch(querryType) {

				case SERVERNAME:
					ICHAT.setServerName(qp.getResponse());
					break;
				case SERVERSTATUS:
					ICM.setServerStatus(ServerStatus.values()[Integer.valueOf(qp.getResponse())]);
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
						fw.readFromStream(bais, true);
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