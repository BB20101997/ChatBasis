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

	@SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
	boolean setActorName(String name,boolean notify);

	@SuppressWarnings("unused")
	boolean isLoggedIn();

	void setUser(BasicUser u);

	BasicUser getUser();

}
