package bb.chat.chat;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.IChatActor;
import bb.chat.network.packet.command.RenamePacket;
import bb.chat.security.BasicUser;
import bb.net.interfaces.IIOHandler;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.util.logging.Logger;

import static bb.chat.base.Constants.LOG_NAME;

/**
 * Created by BB20101997 on 27.03.2015.
 */
@SuppressWarnings("ClassNamePrefixedWithPackageName")
public class ChatActor implements IChatActor {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(ChatActor.class.getName());
		//noinspection DuplicateStringLiteralInspection
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}

	private final IIOHandler iioHandler;
	final         IChat      iChat;
	private       BasicUser  user;
	private String name = "Client";
	private final boolean isDummy;

	public ChatActor(IIOHandler io, IChat ic) {
		this(io, ic,false);
	}

	public ChatActor(IIOHandler io,IChat ic,boolean isDummy){
		iioHandler = io;
		iChat = ic;
		this.isDummy = isDummy;
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
	@SuppressWarnings("PublicMethodWithoutLogging")
	@Override
	public String getActorName() {
		return user == null ? name : user.getUserName();
	}

	//checks if the Actors new name is valid and if sets it, returns true if successful
	@Override
	public boolean setActorName(final String name, final boolean notify) {
		//check if user already exists outside of database
		if(iChat.isActorPresent(name)) {
			return false;
		}

		synchronized(iChat.getUserDatabase()) {
			if(iChat.getUserDatabase() == null || !iChat.getUserDatabase().doesUserExist(name)) {

				log.fine("Setting Actor's name previous " + getActorName() + " new name " + name + ".");


				final String oldName = this.name;

				if(user == null) {
					this.name = name;
				} else {
					user.setUserName(name);
				}

				RenamePacket rn = new RenamePacket(oldName, name);

				if(notify) {
					iChat.getIConnectionManager().sendPackage(rn, iChat.getIConnectionManager().ALL());
				}

				return true;
			}
			return false;
		}
	}

	//returns true if user is logged in
	@SuppressWarnings("PublicMethodWithoutLogging")
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