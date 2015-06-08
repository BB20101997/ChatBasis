package bb.chat.command.subcommands.permission.group;

import bb.chat.command.subcommands.permission.SubPermission;
import bb.chat.interfaces.IChat;
import bb.net.interfaces.IIOHandler;

/**
 * Created by BB20101997 on 15.12.2014.
 */
public class GroupAddGroup extends SubPermission {
	public GroupAddGroup() {
		super("group-add-group");
	}

	@Override
	public void executePermissionCommand(IChat iChat, IIOHandler executor, String cmd, String rest) {
		//TODO
	}
}
