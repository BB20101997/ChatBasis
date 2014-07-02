package bb.chat.interfaces;

/**
 * @author BB20101997
 */
public abstract class ICommand
{

	public boolean backgroundCommand()
	{

		return false;
	}

	public int parameterCount()
	{

		return 0;
	}

	public String[] getAlias()
	{

		return new String[ ]{};
	}

	/**
	 * @return Name of the Command
	 */
	public abstract String getName();

	/**
	 * This function runs when a Command is executed from the Console on the
	 * Server side
	 * 
	 * @param d
	 *            the send message
	 * @param a
	 *            the IMessageHandler that has been used
	 * @param sender
	 *            the Sender of the message in this case the server it self (may
	 *            be removed due to not been necessary)
	 * @return Weather the command was successfully executed
	 */
	public abstract boolean runCommandServer(String d, IMessageHandler a, IChatActor sender);

	/**
	 * This function runs when a Command is executed from the Console on the
	 * Client side
	 * 
	 * @param d
	 *            the send message
	 * @param a
	 *            the IMessageHandle that has been used
	 * @return Weather the command was successfully executed
	 */
	public abstract boolean runCommandClient(String d, IMessageHandler a);

	/**
	 * Runs when a Command is recieved from the Server
	 * 
	 * @param d
	 *            the send message
	 * @param a
	 *            the IMessageHandler that has been used
	 * @return Weather the command was successfully executed
	 */
	public boolean runCommandRecievedFromServer(String d, IMessageHandler a)
	{

		return runCommandClient(d, a);
	}

	/**
	 * Runs when a Command is recieved from the Client
	 * 
	 * @param d
	 *            the send Message
	 * @param a
	 *            the IMessageHandler that has been used
	 * @param sender
	 *            the Client that the Command was recieved from
	 * @return Weather the command was successfully executed
	 */
	public boolean runCommandRecievedFromClient(String d, IMessageHandler a, IChatActor sender)
	{

		return runCommandServer(d, a, sender);
	}

	/**
	 * @return Returns an Array of the Lines to Display when the help Command is
	 *         been executed
	 */
	public String[] helpCommand()
	{

		return new String[ ]{ "Please add a Help Message \n or set the Command to a backGround Command" };
	}

	/**
	 * Should the command be Debugmode only
	 */
	public boolean	DebugOnly	= false;
}