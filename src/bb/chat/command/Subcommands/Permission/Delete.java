package bb.chat.command.subcommands.permission;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.IChatActor;
import bb.chat.security.BasicUser;
import bb.net.interfaces.IIOHandler;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.util.logging.Logger;

/**
 * Created by BB20101997 on 15.12.2014.
 */
@SuppressWarnings("ClassNamingConvention")
public class Delete extends SubPermission {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(Delete.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile("ChatBasis")));
	}

	private static final String PERMISSON = "permission.delete";


	public Delete() {
		super("delete", PERMISSON);
	}

	@Override
	public void executePermissionCommand(IChat iChat, IIOHandler executor, String cmd) {
		log.fine("Executing Command!");
		IChatActor iChatActor = iChat.getActorByIIOHandler(executor);
		if(iChatActor == null) {
			log.fine("Couldn't get Actor!");
			return;
		}
		BasicUser basicUser = iChatActor.getUser();

		if(iChat.getPermissionRegistry().hasPermission(basicUser, perms)) {
			iChat.getPermissionRegistry().removePermission(cmd.split(" ")[1]);
		} else {
			log.fine("Missing Permissions!");
			executor.sendPacket(missingPermsPacket(iChat));
		}

	}
}
