package bb.chat.command.subcommands.permission;

import bb.chat.command.Permission;
import bb.chat.interfaces.IChat;
import bb.net.interfaces.IIOHandler;

import java.util.List;

/**
 * Created by BB20101997 on 15.12.2014.
 */
@SuppressWarnings("ClassNamingConvention")
public class Help extends SubPermission {

	private final Permission perm;

	public Help(Permission instance) {
		super("help");
		perm = instance;
	}

	@Override
	public void executePermissionCommand(IChat iChat, IIOHandler executor, String cmd) {
		List<SubPermission> sp = perm.subCommandList;
		//stop if user can't use help
		if(!checkPerm(executor, iChat)) {
			executor.sendPacket(missingPermsPacket(iChat));
			return;
		}
		for(SubPermission subPermission : sp) {
			//only print help for command that the user can use
			if(subPermission.checkPerm(executor, iChat)) {
				//TODO print help info
			}
		}
	}
}
