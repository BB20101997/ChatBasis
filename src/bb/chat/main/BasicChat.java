package bb.chat.main;

import bb.chat.enums.Side;
import bb.chat.gui.BasicChatPanel;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.IMessageHandler;
import bb.chat.interfaces.IPacketDistributor;
import bb.chat.interfaces.IPacketRegistrie;
import bb.chat.security.BasicPermissionRegistrie;
import bb.chat.security.BasicUserDatabase;

/**
 * Created by BB20101997 on 30.01.2015.
 */
public class BasicChat extends IChat<BasicUserDatabase,BasicPermissionRegistrie> {

	IMessageHandler<BasicUserDatabase,BasicPermissionRegistrie>;

	public BasicChat(){

	}

	@Override
	public IMessageHandler<BasicUserDatabase, BasicPermissionRegistrie> getIMessageHandler() {
		return null;
	}

	@Override
	public IPacketRegistrie getPacketRegistrie() {
		return null;
	}

	@Override
	public BasicPermissionRegistrie getPermissionRegistry() {
		return null;
	}

	@Override
	public IPacketDistributor getPacketDistributor() {
		return null;
	}

	@Override
	public BasicUserDatabase getUserDatabase() {
		return null;
	}

	@Override
	public BasicChatPanel getBasicChatPanel() {
		return null;
	}

	@Override
	public void setBasicChatPanel(BasicChatPanel bcp) {

	}

	@Override
	public Side getSide() {
		return null;
	}
}
