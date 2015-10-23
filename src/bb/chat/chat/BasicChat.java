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
import bb.net.handler.AIConnectionEventHandler;
import bb.net.interfaces.IConnectionManager;
import bb.net.interfaces.IIOHandler;
import bb.util.file.database.FileWriter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by BB20101997 on 30.01.2015.
 */

public class BasicChat implements IChat<BasicUserDatabase, BasicPermissionRegistrie> {

	private static final   File   CONFIGFILE = new File("config.fw").getAbsoluteFile();
	protected static final String LOGNAME    = "bb.chat.chat.BasicChat";

	private final IConnectionManager       imh;
	private final BasicPermissionRegistrie basicPermissionRegistrie;
	private final BasicUserDatabase        basicUserDatabase;
	private final ICommandRegistry         commandRegistry;
	private IBasicChatPanel basicChatPanel = null;
	protected volatile IChatActor localActor;
	private final List<ChatActor> actorList     = new ArrayList<>();
	private final WorkingThread   workingThread = new WorkingThread(this);


	protected String serverMessage = "", serverName = "";
	protected int onlineUserNr = 0, maxOnlineUser = 0;
	protected final List<String> activeUsers = new ArrayList<>();

	public BasicChat(IConnectionManager imessagehandler, BasicPermissionRegistrie bpr, BasicUserDatabase bud, ICommandRegistry icr) {
		this.imh = imessagehandler;
		imh.addConnectionEventHandler(new ConnectionEventHandler());
		basicPermissionRegistrie = bpr;
		basicUserDatabase = bud;
		commandRegistry = icr;
		load();
		Logger.getLogger(InitLoggers.getInstance().BCL).exiting(this.getClass().toString(),this.getClass().getConstructors()[0].toString());
	}

	//gets the actor assimilated with this side
	@Override
	public IChatActor getLocalActor() {
		return localActor;
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
		if(!CONFIGFILE.exists()) {
			try {
				if(!CONFIGFILE.createNewFile()) {
					//TODO
					imh.sendPackage(new ChatPacket("Save file doesn't exist and couldn't be created,changes not saved!", "SYSTEM"), imh.SERVER());
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
		}

	}

	//loads from file
	@Override
	public void load() {

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

			boolean dir;
			dir = CONFIGFILE.getParentFile().exists() || CONFIGFILE.getParentFile().mkdirs();
			boolean file = false;
			try {
				file = CONFIGFILE.createNewFile();
			} catch(IOException e) {
				e.printStackTrace();
			}
			if(!(file & dir)) {
			}
		}

	}

	//shutdown the programm cleanly
	@Override
	public void shutdown() {
		imh.disconnect(imh.ALL());
		workingThread.stop();
	}

	//adds a line to the working threads to process list
	//if necessary starting it
	@Override
	public final void Message(String s) {
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

	//get a list of xonnected users
	@Override
	public final String[] getActiveUserList() {
		return activeUsers.toArray(new String[activeUsers.size()]);
	}

	//set the list of connected users
	@Override
	public final void setActiveUsers(String[] sArgs) {
		synchronized(activeUsers) {
			activeUsers.clear();
			Collections.addAll(activeUsers, sArgs);
		}
	}

	//add a user to connected user list
	@Override
	public final void addActiveUser(String name) {
		synchronized(activeUsers) {
			if(!activeUsers.contains(name)) {
				activeUsers.add(name);
			}
		}
	}

	//remove a user from connected user list
	@Override
	public final void removeActiveUser(String name) {
		synchronized(activeUsers) {
			if(activeUsers.contains(name)) {
				activeUsers.remove(name);
			}
		}
	}

	/**
	 * TODO:the following two methods do not by the time of writing this handel Dummy IIOHandler
	* like e.g. the LOCAL(),ALL(),SERVER() from IConnectionHandler
	**/

	//returns the actor given its string name
	@Override
	public ChatActor getActorByName(String oldName) {
		ChatActor ret = null;
		for(ChatActor ca : actorList) {
			if(ca.getActorName().equals(oldName)) {
				ret =  ca;
				break;
			}
		}
		return ret;
	}

	//gets the actor connected to the iioHandler
	@Override
	public ChatActor getActorByIIOHandler(IIOHandler iioHandler) {
		ChatActor ret = null;
		for(ChatActor ca : actorList) {
			if(ca.getIIOHandler() == iioHandler) {
				ret =  ca;
				break;
			}
		}
		return ret;
	}

	//class to handle ConnectEvent & DisconnectEvent
	public class ConnectionEventHandler extends AIConnectionEventHandler {

		public int i = 1;

		public void handleEvent(ConnectEvent ce) {
			Logger.getLogger(LOGNAME).info("Received ConnectEvent");
			ChatActor ca = new ChatActor(ce.getIIOHandler(), BasicChat.this);
			ca.setActorName("User#" + i++);
			actorList.add(ca);

		}

		public void handleEvent(DisconnectEvent de) {
			Logger.getLogger(LOGNAME).info("Received DisconnectEvent");
			actorList.remove(getActorByIIOHandler(de.getIIOHandler()));
		}

	}
}
