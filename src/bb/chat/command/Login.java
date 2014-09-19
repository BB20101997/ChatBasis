package bb.chat.command;

import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IMessageHandler;
import bb.chat.interfaces.IUserPermission;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class Login implements ICommand {

	@Override
	public boolean initiatePermissionCheck(IUserPermission iup, IMessageHandler imh) {
		return false;
	}

	@Override
	public int maxParameterCount() {
		return 2;
	}

	@Override
	public int minParameterCount() {
		return 1;
	}

	@Override
	public String[] getAlias() {
		return new String[0];
	}

	public String getName() {
		return "login";
	}

	@Override
	public boolean runCommand(String commandLine, IMessageHandler imh) {
		//TODO:Implement function
		return false;
	}

	@Override
	public String[] helpCommand() {
		return new String[0];
	}

	@Override
	public boolean debugModeOnly() {
		return false;
	}
}
