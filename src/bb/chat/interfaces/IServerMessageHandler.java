package bb.chat.interfaces;

import bb.chat.enums.ServerStatus;
import bb.chat.security.BasicPermissionRegistrie;
import bb.chat.security.BasicUserDatabase;

/**
 * Created by BB20101997 on 25.01.2015.
 */
public interface IServerMessageHandler<UD extends BasicUserDatabase,BP extends BasicPermissionRegistrie> extends IMessageHandler<UD,BP> {

	String[] getActiveUserList();
	int getOnlineUsers();
	int getMaxUsers();
	@SuppressWarnings("SameReturnValue")
	String getServerName();
	@SuppressWarnings("SameReturnValue")
	String getServerMessage();
	@SuppressWarnings("SameReturnValue")
	ServerStatus getServerStatus();

}
