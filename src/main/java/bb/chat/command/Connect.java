package bb.chat.command;

import bb.chat.enums.Bundles;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.net.enums.Side;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.text.MessageFormat;
import java.util.logging.Logger;

import static bb.chat.basis.BasisConstants.LOG_NAME;

/**
 * @author BB20101997
 */
@SuppressWarnings({"ClassNamingConvention", "unused"})
public class Connect implements ICommand {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(Connect.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}

	@Override
	public String getName() {
		return Bundles.COMMAND.getString("name.connect");
	}

	@Override
	public void runCommand(String commandLine, IChat iChat) {
		if(iChat.getIConnectionManager().getSide() == Side.CLIENT) {
			String[] strA = commandLine.split(" ");
			iChat.getBasicChatPanel().wipeLog();
			if(strA.length >= 3) {
				iChat.getIConnectionManager().connect(strA[1], Integer.valueOf(strA[2]));
			} else if(strA.length >= 2) {
				iChat.getIConnectionManager().connect(strA[1], 256);
			} else {
				iChat.getIConnectionManager().connect("192.168.178.21", 256);
			}
		}
	}

	@Override
	public String[] helpCommand() {
		//noinspection StringConcatenationMissingWhitespace
		return new String[]{MessageFormat.format(Bundles.COMMAND.getResource().getString("helptext.connect"), COMMAND_INIT_STRING)};
	}
}
