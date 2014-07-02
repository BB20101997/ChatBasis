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

		String[] dS = d.split(" ");
		if(dS.length > 2)
		{
			IChatActor ica = a.getUserByName(dS[1]);
			if(ica != null)
			{
				ica.setActorName(dS[2]);
				return true;
			}

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
		if(dS.length <= 2) { return false; }

		a.setEmpfaenger(IMessageHandler.ALL);
		a.sendMessage(d, a.getActor());
		return true;
	}

	@Override
	public boolean runCommandRecievedFromServer(String d, IMessageHandler a)
	{

		String[] dS = d.split(" ");
		if(dS.length > 2)
		{
			IChatActor ica = a.getUserByName(dS[1]);
			if(ica != null)
			{
				ica.setActorName(dS[2]);
			}
			else if(a.getActor().getActorName().equals(dS[1]))
			{
				a.getActor().setActorName(dS[2]);
				a.println("You are now known as : " + dS[2]);
			}

		}
		return true;
	}
}
