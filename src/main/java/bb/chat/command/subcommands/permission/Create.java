package bb.chat.command.subcommands.permission;

import bb.chat.interfaces.IChat;
import bb.chat.network.packet.chatting.MessagePacket;
import bb.net.interfaces.IIOHandler;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.util.logging.Logger;

import static bb.chat.base.Constants.LOG_NAME;

/**
 * Created by BB20101997 on 15.12.2014.
 */
@SuppressWarnings("ClassNamingConvention")
public class Create extends SubPermission {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(Create.class.getName());
		//noinspection DuplicateStringLiteralInspection
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}


	private static final String PERMISSION = "permission.create";

	public Create() {
		super("create", PERMISSION);
	}

	@Override
	public void executePermissionCommand(IChat iChat, IIOHandler executor, String cmd) {
		log.fine("Executing Command!");
		if(checkPerm(executor,iChat)) {
			iChat.getPermissionRegistry().createPermission(cmd.split(" ",2)[1]);
		} else {
			log.fine("Missing Permissions!");
			executor.sendPacket(new MessagePacket("permission.missing"));
		}

	}
}
