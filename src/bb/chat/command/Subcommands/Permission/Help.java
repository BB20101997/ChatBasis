package bb.chat.command.subcommands.permission;

import bb.chat.command.Permission;
import bb.chat.interfaces.IChat;
import bb.net.interfaces.IIOHandler;

import java.util.List;

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
	public void executePermissionCommand(IChat iChat, IIOHandler executor, String cmd, String rest) {
		@SuppressWarnings("UnusedAssignment") List<SubPermission> sp = perm.subCommandList;
		//TODO
	}
}
