package bb.chat.command.subcommands.permission;

import bb.chat.interfaces.IChat;
import bb.net.interfaces.IIOHandler;

/**
 * Created by BB20101997 on 21. Mai. 2016.
 */
@SuppressWarnings("ClassNamingConvention")
public class List extends SubPermission {

	private static final String PERMISSON = "permission.create";

	public List(){
		super("list",PERMISSON);
	}

	@Override
	public void executePermissionCommand(IChat iChat, IIOHandler executor, String cmd) {

	}
}
