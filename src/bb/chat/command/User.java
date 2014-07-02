package bb.chat.command;

import bb.chat.interfaces.IChatActor;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IMessageHandler;

public class User extends ICommand
{

	@Override
	public String getName()
	{

		return "user";
	}

	@Override
	public boolean runCommandServer(String d, IMessageHandler a, IChatActor sender)
	{

		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean runCommandRecievedFromServer(String d, IMessageHandler a)
	{

		return true;

	}

	@Override
	public boolean runCommandClient(String d, IMessageHandler a)
	{

		return true;
	}

	@Override
	public boolean backgroundCommand()
	{

		return true;
	}

}
