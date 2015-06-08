package bb.chat.interfaces;

import bb.chat.security.BasicUser;
import bb.net.interfaces.IIOHandler;

/**
 * Created by BB20101997 on 27.03.2015.
 */
public interface IChatActor {

	IIOHandler getIIOHandler();

	boolean isDummy();

	String getActorName();

	boolean setActorName(String name);

	boolean isLoggedIn();

	void setUser(BasicUser u);

	BasicUser getUser();

}
