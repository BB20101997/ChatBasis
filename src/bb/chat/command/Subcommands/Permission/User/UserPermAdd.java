package bb.chat.command.Subcommands.Permission.User;

import bb.chat.command.Subcommands.Permission.SubPermission;
import bb.chat.interfaces.IIOHandler;
import bb.chat.interfaces.IMessageHandler;

/**
 * Created by BB20101997 on 15.12.2014.
 */
public class UserPermAdd extends SubPermission {
	public UserPermAdd() {
		super("user-perm-add");
	}

	@Override
	public void executePermissionCommand(IMessageHandler imh, IIOHandler executor, String cmd, String rest){
		//TODO
	}
}
