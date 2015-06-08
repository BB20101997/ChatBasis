package bb.chat.command.subcommands.permission.user;

import bb.chat.command.subcommands.permission.SubPermission;
import bb.chat.interfaces.IChat;
import bb.net.interfaces.IIOHandler;

/**
 * Created by BB20101997 on 15.12.2014.
 */
public class UserDenyRemove extends SubPermission {
	public UserDenyRemove() {
		super("user-deny-remove");
	}

	@Override
	public void executePermissionCommand(IChat iChat, IIOHandler executor, String cmd, String rest) {
		//TODO
	}
}
