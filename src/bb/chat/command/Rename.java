package bb.chat.command;

import bb.chat.interfaces.IChatActor;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IMessageHandler;

/**
 * @author BB20101997
 */
public class Rename extends ICommand
{

	@Override
	public String getName()
	{

		return "rename";
	}

	@Override
	public boolean runCommandServer(String d, IMessageHandler a, IChatActor sender)
	{

		if((sender != null) && (d != null))
		{
			sender.setActorName(d);
			a.sendMessageAll(sender.getActorName() + " has been renamed to " + d, a.getActor());
			return true;
		}
		return false;
	}

	@Override
	public String[] helpCommand()
	{

		return new String[ ]{ "/rename <new Name>", "Used to rename you in Chat!" };
	}

	@Override
	public boolean runCommandClient(String d, IMessageHandler a)
	{

		String[] dS = d.split(" ");
		if(dS.length > 1)
		{
			a.getActor().setActorName(dS[1]);
			a.println("You are now known as : " + dS[1]);
			return true;
		}
		return false;
	}

}
