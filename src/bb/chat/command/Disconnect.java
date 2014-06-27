package bb.chat.command;

import bb.chat.interfaces.IChatActor;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IMessageHandler;

/**
 * @author BB20101997
 */
public class Disconnect extends ICommand
{

	private static String[]	helpMessage	= new String[ ]{ "Disconnects the client from the Server,only executed Client Side" };

	@Override
	public String getName()
	{

		return "disconnect";
	}

	@Override
	public boolean runCommandServer(String d, IMessageHandler a, IChatActor sender)
	{

		a.disconnect(sender);
		return true;
	}

	@Override
	public boolean runCommandClient(String d, IMessageHandler a)
	{

		a.sendMessageAll("/disconnect", a.getActor());
		return false;
	}

	@Override
	public String[] helpCommand()
	{

		return helpMessage;
	}

}
