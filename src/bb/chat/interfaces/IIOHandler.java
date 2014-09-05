package bb.chat.interfaces;

/**
 * Created by BB20101997 on 05.09.2014.
 */
public interface IIOHandler extends Runnable {

	void start();
	void stop();
	boolean isDummy();
	String getActorName();
	void setActorName(String name);
	boolean sendPacket(IPacket p);
	public boolean isAlive();
	void receivedHandshake();
}
