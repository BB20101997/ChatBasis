package bb.chat.interfaces;

import bb.chat.security.BasicPermissionRegistrie;
import bb.chat.security.BasicUserDatabase;
import bb.net.interfaces.IConnectionManager;
import bb.net.interfaces.IIOHandler;

/**
 * Created by BB20101997 on 30.01.2015.
 */
public interface IChat<UD extends BasicUserDatabase, PR extends BasicPermissionRegistrie> {

	/**
	 * Returns the IChatActor that represents this side of the connection
	 * On the Server side this shall not use getSERVERActor to prevent infinite recursion
	 * */
	IChatActor getLOCALActor();

	/**
	 * Returns the IChatActor that represents the Server side, if executed on the Server should return getLOCALActor();
	 * */
	IChatActor getSERVERActor();

	/**
	 * Returns a IChatActor that works as a broadcast to all connections
	 * TODO: should it also broadcast to sender? check implementations!
	 * */
	IChatActor getALLActor();

	IConnectionManager getIConnectionManager();

	PR getPermissionRegistry();

	UD getUserDatabase();

	boolean isDebugMode();

	void setDebugMode(boolean debug);

	IBasicChatPanel getBasicChatPanel();

	void setBasicChatPanel(IBasicChatPanel bcp);

	ICommandRegistry getCommandRegistry();

	//save config etc.to files
	void save();

	//load config etc. form files
	void load();

	//shutdown cleanly
	void shutdown();

	// messages entered by the user should land here
	void message(String s);

	int getMaxUsers();

	void setMaxUsers(int i);

	int getOnlineUsers();

	/**
	 * Deprecated because the amount of online users
	 * should always match the length of getActiveUserList()
	 * therefor it should never be set independently
	 * */
	@Deprecated
	void setOnlineUsers(int i);

	String getServerMessage();

	void setServerMessage(String msg);

	String getServerName();

	void setServerName(String s);

	//TODO either change name or return type to match
	String[] getActiveUserList();

	void setActiveUsers(String[] sArgs);

	void addActiveUser(String name);

	void removeActiveUser(String name);

	IChatActor getActorByName(String name);

	IChatActor getActorByIIOHandler(IIOHandler iioHandler);

	@SuppressWarnings("PublicMethodWithoutLogging")
	default boolean isActorPresent(String name) {
		return getActorByName(name) != null;
	}

}
