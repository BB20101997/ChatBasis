package bb.chat.command.subcommands.permission;

import bb.chat.interfaces.IChat;
import bb.net.interfaces.IIOHandler;

/**
 * Created by BB20101997 on 15.12.2014.
 */
public class Delete extends SubPermission {
	public Delete() {
		super("delete");
	}

	@Override
	public void executePermissionCommand(IChat iChat, IIOHandler executor, String cmd, String rest) {
		//TODO
	}
}
