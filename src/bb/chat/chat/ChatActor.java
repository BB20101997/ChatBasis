package bb.chat.chat;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.IChatActor;
import bb.chat.network.packet.command.RenamePacket;
import bb.chat.security.BasicUser;
import bb.net.interfaces.IIOHandler;

/**
 * Created by BB20101997 on 27.03.2015.
 */
public class ChatActor implements IChatActor {

	final   IIOHandler iioHandler;
	final   IChat      iChat;
	private BasicUser  user;
	private         String  name    = "Client";
	protected final boolean isDummy = false;

	public ChatActor(IIOHandler io, IChat ic) {
		iioHandler = io;
		iChat = ic;
	}
	@Override
	public IIOHandler getIIOHandler() {
		return iioHandler;
	}

	@Override
	public boolean isDummy() {
		return isDummy;
	}

	@Override
	public String getActorName() {

		return user == null ? name : user.getUserName();
	}

	@Override
	public boolean setActorName(String s) {
		synchronized(iChat.getUserDatabase()) {
			if(iChat.getUserDatabase() == null || !iChat.getUserDatabase().doesUserExist(s)) {

				String oldName = name;

				if(user == null) {
					name = s;
				} else {
					//TODO:if server send Packet to Client
					user.setUserName(s);
				}

				RenamePacket rn = new RenamePacket();
				rn.newName = s;
				rn.oldName = oldName;
				iChat.getIConnectionManager().sendPackage(rn, iChat.getIConnectionManager().ALL());

				return true;
			}
			return false;
		}
	}

	@Override
	public boolean isLoggedIn() {
		return user != null;
	}

	@Override
	public BasicUser getUser() {
		return user;
	}

	@Override
	public void setUser(BasicUser u) {
		user = u;
	}

}