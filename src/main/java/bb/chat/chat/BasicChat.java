package bb.chat.chat;

import bb.chat.command.*;
import bb.chat.enums.Bundles;
import bb.chat.interfaces.IBasicChatPanel;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.IChatActor;
import bb.chat.interfaces.ICommandRegistry;
import bb.chat.network.packet.chatting.MessagePacket;
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
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static bb.chat.base.Constants.LOG_NAME;

/**
 * Created by BB20101997 on 30.01.2015.
 */

public class BasicChat implements IChat<BasicUserDatabase, BasicPermissionRegistrie> {

	private static final File CONFIGFILE = new File("config.fw").getAbsoluteFile();

	private final static String BPR_KEY = "permReg";
	private final static String BUD_KEY = "bur";

	private final static Logger LOGGER;

	static {
		LOGGER = Logger.getLogger(BasicChat.class.getName());
		//noinspection DuplicateStringLiteralInspection
		LOGGER.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}

	private IConnectionManager       imh;
	private BasicPermissionRegistrie basicPermissionRegistrie;
	private ICommandRegistry         commandRegistry;
	private BasicUserDatabase        basicUserDatabase;
	private IBasicChatPanel basicChatPanel = null;
	private   boolean    debugMode;
	@SuppressWarnings("unused")
	protected IChatActor LOCAL;
	private   IChatActor ALL;
	private   IChatActor SERVER;

	private final List<IChatActor> actorList     = new ArrayList<>();
	private final WorkingThread    workingThread = new WorkingThread(this);


	private String serverMessage = "", serverName = "";
	private int onlineUserNr = 0, maxOnlineUser = 0;
	private final List<String> activeUsers = new ArrayList<>();

	@SuppressWarnings("unused")
	protected BasicChat(IConnectionManager imessagehandler, BasicPermissionRegistrie bpr, BasicUserDatabase bud, ICommandRegistry icr) {
		this(imessagehandler, bpr, bud, icr, false);
	}

	@SuppressWarnings({"SameParameterValue", "unused"})
	protected BasicChat(IConnectionManager icm, BasicPermissionRegistrie bpr, BasicUserDatabase bud, ICommandRegistry icr, boolean debug) {
		Runtime.getRuntime().addShutdownHook(new Thread(this::save));
		this.imh = icm;
		imh.addConnectionEventHandler(new ConnectionEventHandler());
		basicPermissionRegistrie = bpr;
		basicUserDatabase = bud;
		commandRegistry = icr;
		load();
		LOGGER.exiting(this.getClass().toString(), this.getClass().getConstructors()[0].toString());
		debugMode = debug;
	}

	@SuppressWarnings("PublicMethodWithoutLogging")
	@Override
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
	@Override
	public IChatActor getSERVERActor() {
		if(SERVER == null) {
			SERVER = new ChatActor(getIConnectionManager().SERVER(), this, true) {
				@Override
				public String getActorName() {
					//noinspection DuplicateStringLiteralInspection
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

	@Override
	public boolean isDebugMode() {
		return debugMode;
	}

	@Override
	public void setDebugMode(boolean debug) {
		debugMode = debug;
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
		LOGGER.info("Saving to File!");
		if(!CONFIGFILE.exists()) {
			try {
				if(!CONFIGFILE.createNewFile()) {
					imh.sendPackage(new MessagePacket("save.fail.create"), imh.SERVER());
				}

			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fileWriter = new FileWriter();
		fileWriter.add(basicPermissionRegistrie, BPR_KEY);
		fileWriter.add(basicUserDatabase, BUD_KEY);
		try {
			fileWriter.writeToFile(CONFIGFILE);
		} catch(IOException e) {
			e.printStackTrace();
			LOGGER.warning("Error while saving!");
			//noinspection HardcodedFileSeparator
			imh.sendPackage(new MessagePacket("save.fail.write"), imh.SERVER());
		}

	}

	//loads from file
	@Override
	public void load() {
		LOGGER.fine("Loading from File");
		if(CONFIGFILE.exists()) {
			FileWriter fileWriter = new FileWriter();
			try {
				fileWriter.readFromFile(CONFIGFILE);
				if(fileWriter.containsObject(BPR_KEY)) {
					basicPermissionRegistrie.loadFromFileWriter((FileWriter) fileWriter.get(BPR_KEY));
				}
				if(fileWriter.containsObject(BUD_KEY)) {
					basicUserDatabase.loadFromFileWriter((FileWriter) fileWriter.get(BUD_KEY));
				}
			} catch(IOException e) {
				LOGGER.warning("Failed to load Save! Renaming old, creating new!");
				//noinspection ResultOfMethodCallIgnored,StringConcatenationMissingWhitespace
				CONFIGFILE.renameTo(new File(CONFIGFILE, "-load-error" + new Date().toString()));
			}
		}

		if(!CONFIGFILE.exists()) {
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


	@Override
	protected void finalize() throws Throwable {
		save();
		super.finalize();
	}

	//shutdown the program cleanly
	@Override
	public void shutdown() {
		LOGGER.info("Shutdown initiated");
		imh.disconnect(imh.ALL());
		workingThread.stop();
		basicChatPanel.stop();
	}

	//adds a line to the working threads to process list
	//if necessary starting it
	@Override
	public final void message(String s) {
		LOGGER.fine("Adding Message to queue");
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
	public IChatActor getActorByName(String name) {
		IChatActor ret = null;
		if(getALLActor().getActorName().equals(name)) {
			ret = getALLActor();
		}
		if("LOCAL".equals(name)) {
			ret = getLOCALActor();
		}
		if(getSERVERActor().getActorName().equals(name)) {
			ret = getSERVERActor();
		}
		for(IChatActor ca : actorList) {
			if(ca.getActorName().equals(name)) {
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

	@SuppressWarnings("unused")
	public void addDefaultCommandsClient(){
		addDefaultCommandsBothSides();
	}

	@SuppressWarnings("unused")
	public void addDefaultCommandsServer(){
		addDefaultCommandsBothSides();
		getCommandRegistry().addCommand(new Disconnect());
	}

	private void addDefaultCommandsBothSides(){
		getCommandRegistry().addCommand(new Permission());
		getCommandRegistry().addCommand(new Debug());
		getCommandRegistry().addCommand(new bb.chat.command.List());
		getCommandRegistry().addCommand(new Help());
		getCommandRegistry().addCommand(new Stop());
		getCommandRegistry().addCommand(new Save());
		getCommandRegistry().addCommand(new Register());
		getCommandRegistry().addCommand(new Rename());
		getCommandRegistry().addCommand(new Whisper());
	}

	//class to handle ConnectEvent & DisconnectEvent
	//must be public to be accessible by the event handler
	@SuppressWarnings({"WeakerAccess", "unused"})
	public class ConnectionEventHandler extends EventHandler<IConnectionEvent> {

		public int i = 1;

		@Override
		public void HandleEvent(IConnectionEvent event) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
			if(event instanceof ConnectEvent || event instanceof DisconnectEvent) {
				try {
					super.HandleEvent(event);
				} catch(Exception e) {
					//noinspection StringConcatenationMissingWhitespace
					LOGGER.severe("WTF went wrong here?" + System.lineSeparator() + "This should not even be possible!");
					throw e;
				}
			}
		}

		@SuppressWarnings({"unused", "MethodNamesDifferingOnlyByCase"})
		public void handleEvent(ConnectEvent ce) {
			LOGGER.info("Received ConnectEvent");
			ChatActor ca = new ChatActor(ce.getIIOHandler(), BasicChat.this);
			//noinspection StringConcatenation
			ca.setActorName("User#" + i++, true);
			getBasicChatPanel().println(MessageFormat.format(Bundles.MESSAGE.getResource().getString("rename.join"), getLOCALActor().getActorName(), ca.getActorName()));
			actorList.add(ca);

		}

		@SuppressWarnings("unused")
		public void handleEvent(DisconnectEvent de) {
			LOGGER.info("Received DisconnectEvent");
			IChatActor chatActor = getActorByIIOHandler(de.getIIOHandler());
			//noinspection DuplicateStringLiteralInspection
			MessagePacket mp = new MessagePacket("logout.disconnect", chatActor.getActorName());
			getIConnectionManager().sendPackage(mp, getIConnectionManager().ALL());
			//noinspection DuplicateStringLiteralInspection
			getBasicChatPanel().println(MessageFormat.format(Bundles.MESSAGE.getResource().getString("logout.disconnect"), chatActor.getActorName()));
			actorList.remove(chatActor);
		}

	}
}