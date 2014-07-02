package bb.chat.interfaces;

/**
 * @author BB20101997
 */
public interface IChatActor
{

	/**
	 * @return the Actors Name
	 */
	public String getActorName();

	/**
	 * @param s
	 *            sets the Actors name to s;
	 */
	public void setActorName(String s);

	public void disconnect();
}
