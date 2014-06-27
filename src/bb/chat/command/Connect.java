package bb.chat.command;

import bb.chat.interfaces.IChatActor;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IMessageHandler;

/**
 * @author BB20101997
 */
public class Connect extends ICommand
{

	/**
	 * Used Client-Side to Connect to a Server
	 */
	public Connect()
	{

		ClientOnly = true;
		ServerOnly = false;
	}

	@Override
	public String getName()
	{

		return "connect";
	}

	@Override
	public boolean runCommandServer(String d, IMessageHandler a, IChatActor sender)
	{

		return false;
	}

	@Override
	public boolean runCommandClient(String d, IMessageHandler a)
	{

		String[] strA = d.split(" ");
		a.connect(strA[1], Integer.valueOf(strA[2]));
		return true;
	}

	@Override
	public String[] helpCommand()
	{

		// TODO : Fill in help for connect
		return null;
	}

}
