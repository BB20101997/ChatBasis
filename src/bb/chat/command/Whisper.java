package bb.chat.command;

import bb.chat.interfaces.IChatActor;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IMessageHandler;

/**
 * @author BB20101997
 */
public class Whisper extends ICommand
{

	@Override
	public String getName()
	{

		return "whisper";
	}

	@Override
	public boolean runCommandServer(String d, IMessageHandler a, IChatActor client)
	{

		String str[] = d.split(" ", 3);
		if(str.length > 2)
		{
			System.out.println(str[1] + " : " + str[2]);
			a.setEmpfaenger(a.getUserByName(str[1]));
			a.sendMessage(str[2], client);
			return true;
		}
		return false;
	}

	@Override
	public String[] helpCommand()
	{

		return new String[ ]{ "Usage : /whisper <ToPlayer> <Message>" };
	}

	@Override
	public boolean runCommandClient(String d, IMessageHandler a)
	{

		if((d.split(" ", 3).length <= 2)) { return false; }
		a.setEmpfaenger(IMessageHandler.SERVER);
		a.sendMessage(d, a.getActor());
		return true;
	}

}
