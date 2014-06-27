package bb.chat.command;

import bb.chat.interfaces.IChatActor;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IMessageHandler;

/**
 * @author BB20101997
 */
@Deprecated
// Will be replaced by disconnect
public class Logout extends ICommand
{

	/**
	 * 
	 */
	public Logout()
	{

		ClientOnly = false;
		ServerOnly = true;
	}

	@Override
	public String getName()
	{

		return "logout";
	}

	@Override
	public boolean runCommandServer(String d, IMessageHandler a, IChatActor sender)
	{

		// IClientListener cli = a.getClient(sender);
		// cli.end();
		// a.sendMessage("logout",sender,"Server");
		return true;
	}

	@Override
	public String[] helpCommand()
	{

		return new String[ ]{ "Used to logout by Client when GUI is closed!" };
	}

	@Override
	public boolean runCommandClient(String d, IMessageHandler a)
	{

		return false;
	}

}
