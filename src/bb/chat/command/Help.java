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

		String[] helps = d.getHelpForAllCommands();
		StringBuilder s = new StringBuilder();

		s.append("Help for the server side commands:");
		s.append("\n");

		for(String str : helps)
		{
			s.append(str);
			s.append("\n");
		}

		String str = s.toString();
		d.setEmpfaenger(sender);
		d.sendMessage(str, sender);
		System.out.println("Executing Help Command");
		return true;
	}

	@Override
	public String[] helpCommand()
	{

		return new String[ ]{ "This will Display the help messages!" };
	}

	@Override
	public boolean runCommandClient(String d, IMessageHandler a)
	{

		String[] helps = a.getHelpForAllCommands();
		StringBuilder s = new StringBuilder();

		s.append("Help for the client side commands:");
		s.append("\n");

		for(String str : helps)
		{
			s.append(str);
			s.append("\n");
		}

		String str = s.toString();
		a.setEmpfaenger(IMessageHandler.SERVER);
		a.sendMessage(str, a.getActor());
		return false;
	}

}
