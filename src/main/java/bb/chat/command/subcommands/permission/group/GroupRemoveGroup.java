package bb.chat.command.subcommands.permission.group;

import bb.chat.command.subcommands.permission.SubPermission;
import bb.chat.interfaces.IChat;
import bb.net.interfaces.IIOHandler;

/**
 * Created by BB20101997 on 15.12.2014.
 */
@SuppressWarnings("ClassNamePrefixedWithPackageName")
public class GroupRemoveGroup extends SubPermission {
	public GroupRemoveGroup() {
		super("group-remove-group");
	}

	@Override
	public void executePermissionCommand(IChat iChat, IIOHandler executor, String cmd) {
		//TODO

	}
}
