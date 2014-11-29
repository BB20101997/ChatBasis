package bb.chat.interfaces;

/**
 * Created by BB20101997 on 05.09.2014.
 */
public interface IIOHandler<U extends IUserPermission> extends Runnable {

	void start();

	void stop();

	boolean isDummy();

	String getActorName();

	void setActorName(String name);

	boolean sendPacket(IPacket p);

	public boolean isAlive();

	void receivedHandshake();

	U getUserPermission();

	void setUserPermission(U u);

	boolean isLogedIn();

	IUser<U> getUser();

	void setUser(IUser<U> u);
}
