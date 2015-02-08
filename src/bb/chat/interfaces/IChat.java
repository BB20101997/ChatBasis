package bb.chat.interfaces;

import bb.chat.security.BasicPermissionRegistrie;
import bb.chat.security.BasicUserDatabase;

/**
 * Created by BB20101997 on 30.01.2015.
 */
public interface IChat<UD extends BasicUserDatabase,PR extends BasicPermissionRegistrie> {

	IConnectionHandler<UD,PR> getIMessageHandler();

	IPacketRegistrie getPacketRegistrie();

	PR getPermissionRegistry();

	IPacketDistributor getPacketDistributor();

	UD getUserDatabase();

	IBasicChatPanel getBasicChatPanel();

	ICommandRegistry getCommandRegestry();

	void setBasicChatPanel(IBasicChatPanel bcp);

	void save();

	void load();

	void shutdown();
}
