package bb.chat.command.Subcommands.Permission.Group;

import bb.chat.command.Subcommands.Permission.SubPermission;
import bb.chat.interfaces.IIOHandler;
import bb.chat.interfaces.IConnectionHandler;

/**
 * Created by BB20101997 on 15.12.2014.
 */
public class GroupPermAdd extends SubPermission {
	public GroupPermAdd() {
		super("group-perm-add");
	}

	@Override
	public void executePermissionCommand(IConnectionHandler imh, IIOHandler executor, String cmd, String rest) {
		//TODO
	}
}
