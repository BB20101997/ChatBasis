package bb.chat.command;

import bb.chat.enums.Bundles;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.util.logging.Logger;

import static bb.chat.basis.BasisConstants.LOG_NAME;

/**
 * Created by BB20101997 on 19. Mai. 2016.
 */
@SuppressWarnings({"ClassNamingConvention", "unused"})
public class Debug implements ICommand {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(Debug.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}
	
	@Override
	public String getName() {
		return Bundles.COMMAND.getString("name.debug");
	}

	@Override
	public void runCommand(String commandLine, IChat iChat) {
		iChat.getBasicChatPanel().println(Bundles.COMMAND.getString("debug.sparta"));
		String p = commandLine.split(" ")[1];
		iChat.getBasicChatPanel().println(String.valueOf(iChat.getPermissionRegistry().isPermissionRegistered(p)));
	}

	@Override
	public boolean isDebugModeOnly() {
		return true;
	}
}
