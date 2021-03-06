package bb.chat.command.subcommands.permission;

import bb.chat.basis.BasisConstants;
import bb.chat.enums.Bundles;
import bb.chat.interfaces.IChat;
import bb.chat.network.packet.chatting.MessagePacket;
import bb.net.interfaces.IIOHandler;

import java.text.MessageFormat;
import java.util.logging.Logger;

/**
 * Created by BB20101997 on 15.12.2014.
 */
@SuppressWarnings("ClassNamingConvention")
public class Create extends SubPermission {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger logger = BasisConstants.getLogger(Create.class);

	private static final String[] PERMISSION = new String[]{"permission.create"};

	public Create() {
		super(PERMISSION);
	}

	@Override
	public String getSubName(){
		return Bundles.COMMAND.getString("name.sub.create");
	}

	@Override
	public void executePermissionCommand(IChat iChat, IIOHandler executor, String cmd) {
		if(checkPerm(executor,iChat)) {
			iChat.getPermissionRegistry().createPermission(cmd.split(" ",2)[1]);
		} else {
			logger.fine(MessageFormat.format(Bundles.LOG_TEXT.getString(LOG_MISSING_PERM), getName()));
			executor.sendPacket(new MessagePacket(MISSING_PERM));
		}

	}
}
