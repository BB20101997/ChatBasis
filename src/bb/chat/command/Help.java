package bb.chat.command;

import bb.chat.interfaces.IChatActor;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IMessageHandler;

/**
 * @author BB20101997
 */
public class Help extends ICommand
{

	@Override
	public String getName()
	{

		return "help";
	}

	@Override
	public boolean runCommandServer(String a, IMessageHandler d, IChatActor sender)
	{

		d.helpAll(sender);
		System.out.println("Executing Help Command");
		return true;
	}

	@Override
	public String[] helpCommand()
	{

		return new String[ ]{ "This will Display the help message!" };
	}

	@Override
	public boolean runCommandClient(String d, IMessageHandler a)
	{

		a.helpAll(a.getActor());
		return false;
	}

}
