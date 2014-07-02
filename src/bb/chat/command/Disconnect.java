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

		return true;
	}

	@Override
	public boolean runCommandClient(String d, IMessageHandler a)
	{

		a.setEmpfaenger(IMessageHandler.SERVER);
		a.sendMessage("/disconnect", a.getActor());
		return true;
	}

	@Override
	public boolean runCommandRecievedFromServer(String d, IMessageHandler a)
	{

		return true;
	}

	@Override
	public boolean runCommandRecievedFromClient(String d, IMessageHandler a, IChatActor sender)
	{

		a.disconnect(sender);
		return true;
	}

	@Override
	public String[] helpCommand()
	{

		return helpMessage;
	}

}
