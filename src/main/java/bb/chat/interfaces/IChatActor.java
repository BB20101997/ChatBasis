package bb.chat.interfaces;

import bb.chat.security.BasicUser;
import bb.net.interfaces.IIOHandler;

/**
 * Created by BB20101997 on 27.03.2015.
 */
public interface IChatActor {

	IIOHandler getIIOHandler();

	//return if this is not a real connection e.g. Loopback, Broadcast
	@SuppressWarnings("unused")
	boolean isDummy();

	String getActorName();

	@Deprecated
	@SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
	default boolean setActorName(String name){
		return setActorName(name,true);
	}

	/**
	 *@param name The Name the Actor shall get
	 *@param notify if sb shall be notified about the name change
	 * if the Server send the name change to the client the client shouldn't notify the Server,
	 * while a name change on the Server should notify the Client
	 *
	 * */
	@SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
	boolean setActorName(String name,boolean notify);

	@SuppressWarnings("unused")
	boolean isLoggedIn();

	void setUser(BasicUser user);

	BasicUser getUser();

}
