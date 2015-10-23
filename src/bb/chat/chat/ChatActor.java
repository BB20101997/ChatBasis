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

	//gets the IIOHandler that handles the ChatActors connection
	@Override
	public IIOHandler getIIOHandler() {
		return iioHandler;
	}

	//returns if this is a dummy Actor
	@Override
	public boolean isDummy() {
		return isDummy;
	}

	//returns the Actors name
	@Override
	public String getActorName() {

		return user == null ? name : user.getUserName();
	}

	//checks if the Actors name is valid and if sets it returns if true if successful
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

	//returns true if user is logged in
	@Override
	public boolean isLoggedIn() {
		return user != null;
	}

	//returns the user object if logged in else null
	@Override
	public BasicUser getUser() {
		return user;
	}

	//set the user object associated
	@Override
	public void setUser(BasicUser u) {
		user = u;
	}

}