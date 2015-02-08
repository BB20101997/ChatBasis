package bb.chat.command.Subcommands.Permission.User;

import bb.chat.command.Subcommands.Permission.SubPermission;
import bb.chat.interfaces.IIOHandler;
import bb.chat.interfaces.IConnectionHandler;

/**
 * Created by BB20101997 on 15.12.2014.
 */
public class UserDenyAdd extends SubPermission {
	public UserDenyAdd() {
		super("user-deny-add");
	}

	@Override
	public void executePermissionCommand(IConnectionHandler imh, IIOHandler executor, String cmd, String rest) {
		//TODO
	}
}
