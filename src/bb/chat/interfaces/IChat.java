package bb.chat.interfaces;

import bb.chat.security.BasicPermissionRegistrie;
import bb.chat.security.BasicUserDatabase;
import bb.net.interfaces.IConnectionManager;
import bb.net.interfaces.IIOHandler;

/**
 * Created by BB20101997 on 30.01.2015.
 */
public interface IChat<UD extends BasicUserDatabase, PR extends BasicPermissionRegistrie> {

	IChatActor getLOCALActor();

	IConnectionManager getIConnectionManager();

	PR getPermissionRegistry();

	UD getUserDatabase();

	IBasicChatPanel getBasicChatPanel();

	ICommandRegistry getCommandRegistry();

	void setBasicChatPanel(IBasicChatPanel bcp);

	//save to files
	void save();

	//load form files
	void load();

	//shutdown cleanly
	void shutdown();

	// messages entered by the user should land here
	void Message(String s);

	int getMaxUsers();

	void setMaxUsers(int i);

	int getOnlineUsers();

	void setOnlineUsers(int i);

	String getServerMessage();

	void setServerMessage(String msg);

	String getServerName();

	void setServerName(String s);

	String[] getActiveUserList();

	void setActiveUsers(String[] sArgs);

	void addActiveUser(String name);

	void removeActiveUser(String name);

	IChatActor getActorByName(String oldName);

	IChatActor getActorByIIOHandler(IIOHandler iioHandler);

	@SuppressWarnings("PublicMethodWithoutLogging")
	default boolean isActorPresent(String name){
		return getActorByName(name)!=null;
	}

}
