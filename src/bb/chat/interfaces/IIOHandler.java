package bb.chat.interfaces;

/**
 * Created by BB20101997 on 05.09.2014.
 */
public interface IIOHandler<T, P extends IPermission<T>, G extends IUserPermissionGroup<T, P, G>> extends Runnable {

	void start();

	void stop();

	boolean isDummy();

	String getActorName();

	void setActorName(String name);

	boolean sendPacket(IPacket p);

	public boolean isAlive();

	void receivedHandshake();

	IUserPermission<T, P, G> getUserPermission();
}
