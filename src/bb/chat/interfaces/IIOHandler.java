package bb.chat.interfaces;

import bb.chat.enums.NetworkState;
import bb.chat.security.BasicUser;

/**
 * Created by BB20101997 on 05.09.2014.
 */
public interface IIOHandler extends Runnable {

	void start();

	void stop();

	boolean isDummy();

	String getActorName();

	boolean setActorName(String name);

	boolean sendPacket(IPacket p);

	public boolean isAlive();

	void receivedHandshake();

	NetworkState getNetworkState();

	boolean isLoggedIn();

	BasicUser getUser();

	void setUser(BasicUser u);
}
