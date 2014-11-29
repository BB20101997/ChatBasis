package bb.chat.command;

import bb.chat.enums.Side;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IMessageHandler;
import bb.chat.interfaces.IPacket;
import bb.chat.network.packet.PermissionPacket;

/**
 * Created by BB20101997 on 28.11.2014.
 */
public class Permission implements ICommand {

	private static final String[] alias = new String[]{"permission-deny-add", "permission-allow-add", "permission-deny-remove", "permission-allow-remove", "permission-group-add", "permission-group-remove", "permission-help"};

	@Override
	public int maxParameterCount() {
		return 2;
	}

	@Override
	public int minParameterCount() {
		return 2;
	}

	@Override
	public String[] getAlias() {
		return alias;
	}

	@Override
	public String getName() {
		return "permission";
	}

	@Override
	public void runCommand(String commandLine, IMessageHandler imh) {
		String[] command = commandLine.split(" ", 3);

		if("permission-help".equals(command[0])) {
			//TODO:Add help
		} else {
			if(imh.getSide() == Side.CLIENT) {
				IPacket p = new PermissionPacket(command[0], command[1], command[2]);
				imh.sendPackage(p);
			} else {
				imh.getPermissionRegistry().setPermission(IMessageHandler.SERVER, imh.getUserByName(command[1]), command[0], command[2]);
			}
		}
	}

	@Override
	public String[] helpCommand() {
		return new String[0];
	}

	@Override
	public boolean debugModeOnly() {
		return false;
	}
}
