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
		if(strA.length >= 3)
		{
			a.connect(strA[1], Integer.valueOf(strA[2]));
			return true;
		}
		else if(strA.length >= 2)
		{
			a.connect(strA[1], 256);
			return true;
		}
		return false;
	}
}
