package bb.chat.command.subcommands.permission;

import bb.chat.interfaces.IChat;
import bb.net.interfaces.IIOHandler;

/**
 * Created by BB20101997 on 21. Mai. 2016.
 */
@SuppressWarnings("ClassNamingConvention")
public class List extends SubPermission {

	private static final String PERMISSION = "permission.list";

	public List(){
		super("list", PERMISSION);
	}

	@Override
	public void executePermissionCommand(IChat iChat, IIOHandler executor, String cmd) {
		if(checkPerm(executor,iChat)){
			String[] perms = iChat.getPermissionRegistry().getPermissionsRegistered();
			iChat.getBasicChatPanel().println("The following Permissions are registered:");
			for(String s:perms){
				//noinspection StringConcatenation
				iChat.getBasicChatPanel().println(" - "+s);
			}
			iChat.getBasicChatPanel().println("End of Permission List!");
		}
	}
}
