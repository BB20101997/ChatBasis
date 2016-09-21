package bb.chat.command;

import bb.chat.enums.Bundles;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.net.enums.Side;
import bb.net.packets.connecting.DisconnectPacket;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.util.logging.Logger;

import static bb.chat.basis.BasisConstants.LOG_NAME;

/**
 * @author BB20101997
 */
@SuppressWarnings("unused")
public class Disconnect implements ICommand {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(Disconnect.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}

	@Override
	public String getName() {

		return Bundles.COMMAND.getString("name.disconnect");
	}

	@Override
	public void runCommand(String commandLine, IChat iChat) {
		if(iChat.getIConnectionManager().getSide() == Side.CLIENT) {
			iChat.getIConnectionManager().sendPackage(new DisconnectPacket(), iChat.getIConnectionManager().SERVER());
			iChat.getIConnectionManager().disconnect(iChat.getLOCALActor().getIIOHandler());
			iChat.getBasicChatPanel().wipeLog();
			iChat.getBasicChatPanel().println(Bundles.COMMAND.getResource().getString("disconnect.success"));
		}
	}

	@Override
	public String[] helpCommand() {
		return new String[]{Bundles.COMMAND.getResource().getString("helptext.disconnect")};
	}

	@Override
	public boolean isDebugModeOnly() {
		return false;
	}

}
