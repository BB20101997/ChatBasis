package bb.chat.chat;

import bb.chat.interfaces.IBasicChatPanel;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.IChatActor;
import bb.chat.interfaces.ICommandRegistry;
import bb.chat.network.packet.chatting.ChatPacket;
import bb.chat.security.BasicPermissionRegistrie;
import bb.chat.security.BasicUserDatabase;
import bb.net.event.ConnectEvent;
import bb.net.event.DisconnectEvent;
import bb.net.interfaces.IConnectionEvent;
import bb.net.interfaces.IConnectionManager;
import bb.net.interfaces.IIOHandler;
import bb.util.event.EventHandler;
import bb.util.file.database.FileWriter;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by BB20101997 on 30.01.2015.
 */

public class BasicChat implements IChat<BasicUserDatabase, BasicPermissionRegistrie> {

	private static final File CONFIGFILE = new File("config.fw").getAbsoluteFile();

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(BasicChat.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile("ChatBasis")));
	}

	private final IConnectionManager       imh;
	private final BasicPermissionRegistrie basicPermissionRegistrie;
	private final BasicUserDatabase        basicUserDatabase;
	private final ICommandRegistry         commandRegistry;
	private IBasicChatPanel basicChatPanel = null;
	protected IChatActor LOCAL;
	private   IChatActor ALL;
	private   IChatActor SERVER;

	private final List<IChatActor> actorList     = new ArrayList<>();
	private final WorkingThread    workingThread = new WorkingThread(this);


	private String serverMessage = "", serverName = "";
	private int onlineUserNr = 0, maxOnlineUser = 0;
	private final List<String> activeUsers = new ArrayList<>();

	protected BasicChat(IConnectionManager imessagehandler, BasicPermissionRegistrie bpr, BasicUserDatabase bud, ICommandRegistry icr) {
		this.imh = imessagehandler;
		imh.addConnectionEventHandler(new ConnectionEventHandler());
		basicPermissionRegistrie = bpr;
		basicUserDatabase = bud;
		commandRegistry = icr;
		load();
		log.exiting(this.getClass().toString(), this.getClass().getConstructors()[0].toString());
	}

	@SuppressWarnings("PublicMethodWithoutLogging")
	public IChatActor getALLActor() {
		if(ALL == null) {
			ALL = new ChatActor(getIConnectionManager().ALL(), this, true) {
				@Override
				public String getActorName() {
					return "ALL";
				}
			};
		}
		return ALL;
	}

	@SuppressWarnings("PublicMethodWithoutLogging")
	public IChatActor getSERVERActor() {
		if(SERVER == null) {
			SERVER = new ChatActor(getIConnectionManager().SERVER(), this, true) {
				@Override
				public String getActorName() {
					return "SERVER";
				}
			};
		}
		return SERVER;
	}

	//gets the actor assimilated with this side
	@Override
	public IChatActor getLOCALActor() {
		return LOCAL;
	}

	//returns the IConnectionManager
	@Override
	public IConnectionManager getIConnectionManager() {
		return imh;
	}

	//returns the Permission Registry
	@Override
	public BasicPermissionRegistrie getPermissionRegistry() {
		return basicPermissionRegistrie;
	}

	//returns the UserDatabase
	@Override
	public BasicUserDatabase getUserDatabase() {
		return basicUserDatabase;
	}

	//returns the BasicChatPanel may be null
	@Override
	public IBasicChatPanel getBasicChatPanel() {
		return basicChatPanel;
	}

	//sets the BasicChatPanel
	@Override
	public void setBasicChatPanel(IBasicChatPanel bcp) {
		basicChatPanel = bcp;
	}

	//returns the CommendRegistry
	@Override
	public ICommandRegistry getCommandRegistry() {
		return commandRegistry;
	}

	//saves everything to file
	@Override
	public void save() {
		log.info("Saving to File!");
		if(!CONFIGFILE.exists()) {
			try {
				if(!CONFIGFILE.createNewFile()) {
					imh.sendPackage(new ChatPacket("Save file doesn't exist and couldn't be created,changes probably not saved!", "SYSTEM"), imh.SERVER());
				}

			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fileWriter = new FileWriter();
		fileWriter.add(basicPermissionRegistrie, "permReg");
		fileWriter.add(basicUserDatabase, "bur");
		try {
			fileWriter.writeToFile(CONFIGFILE);
		} catch(IOException e) {
			e.printStackTrace();
			log.warning("Error while saving!");
			//noinspection HardcodedFileSeparator
			imh.sendPackage(new ChatPacket("Couldn't write saves to file! I/OException", "SYSTEM"), imh.SERVER());
		}

	}

	//loads from file
	@Override
	public void load() {
		log.fine("Loading from File");
		if(CONFIGFILE.exists()) {
			FileWriter fileWriter = new FileWriter();
			try {
				fileWriter.readFromFile(CONFIGFILE);
				if(fileWriter.containsObject("permReg")) {
					basicPermissionRegistrie.loadFromFileWriter((FileWriter) fileWriter.get("permReg"));
				}
				if(fileWriter.containsObject("bur")) {
					basicUserDatabase.loadFromFileWriter((FileWriter) fileWriter.get("bur"));
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else {

			if(!CONFIGFILE.getParentFile().exists()) {
				//noinspection ResultOfMethodCallIgnored
				CONFIGFILE.getParentFile().mkdirs();
			}
			try {
				//noinspection ResultOfMethodCallIgnored
				CONFIGFILE.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}

	}

	//shutdown the program cleanly
	@Override
	public void shutdown() {
		log.info("Shutdown initiated");
		imh.disconnect(imh.ALL());
		workingThread.stop();
	}

	//adds a line to the working threads to process list
	//if necessary starting it
	@Override
	public final void Message(String s) {
		log.fine("Adding Message to queue");
		workingThread.start();
		workingThread.addLine(s);
	}

	//max users allowed to connect
	@Override
	public final int getMaxUsers() {
		return maxOnlineUser;
	}

	//change the maximal number of users allowed to connect
	@Override
	public final void setMaxUsers(int i) {
		maxOnlineUser = i;
	}

	//returns the amount of connected Users
	@Override
	public final int getOnlineUsers() {
		return onlineUserNr;
	}

	//updates the amount of connected users
	@Override
	public final void setOnlineUsers(int i) {
		onlineUserNr = i;
	}

	//returns the Server Message
	@Override
	public final String getServerMessage() {
		return serverMessage;
	}

	//sets the servers Message
	@Override
	public final void setServerMessage(String msg) {
		serverMessage = msg;
	}

	//returns the servers name
	@Override
	public final String getServerName() {
		return serverName;
	}

	//sets the servers name
	@Override
	public final void setServerName(String s) {
		serverName = s;
	}

	//get a list of connected users
	@SuppressWarnings("PublicMethodWithoutLogging")
	@Override
	public final String[] getActiveUserList() {
		return activeUsers.toArray(new String[activeUsers.size()]);
	}

	//set the list of connected users
	//not sure if useful
	@SuppressWarnings("PublicMethodWithoutLogging")
	@Override
	public final void setActiveUsers(String[] sArgs) {
		synchronized(activeUsers) {
			activeUsers.clear();
			Collections.addAll(activeUsers, sArgs);
		}
	}

	//add a user to connected user list
	@SuppressWarnings("PublicMethodWithoutLogging")
	@Override
	public final void addActiveUser(String name) {
		synchronized(activeUsers) {
			if(!activeUsers.contains(name)) {
				activeUsers.add(name);
			}
		}
	}

	//remove a user from connected user list
	@SuppressWarnings("PublicMethodWithoutLogging")
	@Override
	public final void removeActiveUser(String name) {
		synchronized(activeUsers) {
			if(activeUsers.contains(name)) {
				activeUsers.remove(name);
			}
		}
	}

	//returns the actor given its string name
	@SuppressWarnings("PublicMethodWithoutLogging")
	@Override
	public IChatActor getActorByName(String oldName) {
		IChatActor ret = null;
		if("ALL".equals(oldName)) {
			ret = getALLActor();
		}
		if("LOCAL".equals(oldName)) {
			ret = getLOCALActor();
		}
		if("SERVER".equals(oldName)) {
			ret = getSERVERActor();
		}
		for(IChatActor ca : actorList) {
			if(ca.getActorName().equals(oldName)) {
				ret = ca;
				break;
			}
		}
		return ret;
	}

	//gets the actor connected to the iioHandler
	@SuppressWarnings("PublicMethodWithoutLogging")
	@Override
	public IChatActor getActorByIIOHandler(IIOHandler iioHandler) {

		if(iioHandler == getIConnectionManager().ALL()) {
			return getALLActor();
		}
		if(iioHandler == getIConnectionManager().LOCAL()) {
			return getLOCALActor();
		}
		if(iioHandler == getIConnectionManager().SERVER()) {
			return getSERVERActor();
		}
		for(IChatActor ca : actorList) {
			if(ca.getIIOHandler() == iioHandler) {
				return ca;
			}
		}
		return null;
	}

	//class to handle ConnectEvent & DisconnectEvent
	//must be public to be accessible by the event handler
	@SuppressWarnings("WeakerAccess")
	public class ConnectionEventHandler extends EventHandler<IConnectionEvent> {

		public int i = 1;

		@Override
		public void HandleEvent(IConnectionEvent event) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
			if(event instanceof ConnectEvent || event instanceof DisconnectEvent) {
				try {
					super.HandleEvent(event);
				} catch(Exception e) {
					//noinspection StringConcatenationMissingWhitespace
					log.severe("WTF whent wrong here?" + System.lineSeparator() + "This should not even be possible!");
					throw e;
				}
			}
		}

		@SuppressWarnings("unused")
		public void handleEvent(ConnectEvent ce) {
			log.info("Received ConnectEvent");
			ChatActor ca = new ChatActor(ce.getIIOHandler(), BasicChat.this);
			ca.setActorName("User#" + i++, true);
			getBasicChatPanel().println("[" + getLOCALActor().getActorName() + "] " + ca.getActorName() + " joined the Chat!");
			actorList.add(ca);

		}

		@SuppressWarnings("unused")
		public void handleEvent(DisconnectEvent de) {
			log.info("Received DisconnectEvent");
			IChatActor chatActor = getActorByIIOHandler(de.getIIOHandler());
			ChatPacket p = new ChatPacket(chatActor.getActorName() + " disconnected!", "LOGOUT");
			getIConnectionManager().sendPackage(p, getIConnectionManager().ALL());
			getBasicChatPanel().println("[LOGOUT] " + chatActor.getActorName() + " disconnected!");
			actorList.remove(chatActor);
		}

	}
}