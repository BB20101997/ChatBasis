package bb.chat.command.Subcommands.Permission;

import bb.chat.interfaces.IIOHandler;
import bb.chat.interfaces.IMessageHandler;

/**
 * Created by BB20101997 on 15.12.2014.
 */
public class Create extends SubPermission {

	public Create() {
		super("create");
	}

	@Override
	public void executePermissionCommand(IMessageHandler imh, IIOHandler executor, String cmd, String rest) {
		//TODO
	}
}
