package bb.chat.command;

import bb.chat.enums.Bundles;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.command.WhisperPacket;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.text.MessageFormat;
import java.util.logging.Logger;

import static bb.chat.basis.BasisConstants.LOG_NAME;

/**
 * @author BB20101997
 */

public class Whisper implements ICommand {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger logger;

	static {
		logger = Logger.getLogger(Whisper.class.getName());
		logger.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}

	@Override
	public String[] getAlias() {
		return new String[]{Bundles.COMMAND.getString("alias.1.whisper")};
	}

	@Override
	public String getName() {
		return Bundles.COMMAND.getString("name.whisper");
	}

	@Override
	public void runCommand(String commandLine, IChat iChat) {
		logger.fine("Pssst just whisper!");
		String[] com = commandLine.split(" ", 3);
		if(com.length <= 2) {
			return;
		}
		iChat.getIConnectionManager().sendPackage(new WhisperPacket(iChat.getLOCALActor().getActorName(), com[2], com[1]), iChat.getIConnectionManager().SERVER());
		iChat.getBasicChatPanel().println(MessageFormat.format(Bundles.COMMAND.getResource().getString("whisper.sender"),com[1],com[2]));
	}

	@Override
	public String[] helpCommand() {
		return new String[]{MessageFormat.format(Bundles.COMMAND.getResource().getString("helptext.whisper"), COMMAND_INIT_STRING)};
	}

}
