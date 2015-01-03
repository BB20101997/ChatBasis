package bb.chat.command.Subcommands.Permission;

import bb.chat.command.Permission;
import bb.chat.interfaces.IIOHandler;
import bb.chat.interfaces.IMessageHandler;

/**
 * Created by BB20101997 on 15.12.2014.
 */
public class Help extends SubPermission {
	private final Permission perm;
	public Help(Permission instance) {
		super("help");
		perm = instance;
	}

	@Override
	public void executePermissionCommand(IMessageHandler imh, IIOHandler executor, String cmd, String rest) {
		//TODO
	}
}
