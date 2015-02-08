package bb.chat.command.Subcommands.Permission.Group;

import bb.chat.command.Subcommands.Permission.SubPermission;
import bb.chat.interfaces.IIOHandler;
import bb.chat.interfaces.IConnectionHandler;

/**
 * Created by BB20101997 on 15.12.2014.
 */
public class GroupAddGroup extends SubPermission {
	public GroupAddGroup() {
		super("group-add-group");
	}

	@Override
	public void executePermissionCommand(IConnectionHandler imh, IIOHandler executor, String cmd, String rest) {
		//TODO
	}
}
