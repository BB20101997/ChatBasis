package bb.chat.command;

import bb.chat.enums.Bundles;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.chatting.ChatPacket;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.text.MessageFormat;
import java.util.logging.Logger;

import static bb.chat.basis.BasisConstants.LOG_NAME;

/**
 * @author BB20101997
 */
@SuppressWarnings({"ClassNamingConvention", "unused"})
public class Help implements ICommand {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(Help.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}


	@Override
	public String getName() {

		return "help";
	}

	@Override
	public void runCommand(String commandLine, IChat iChat) {

		//TODO:FIX runCommand
		//TODO: add a possibility to get server side commands on the client, also maybe needs changes to the command registry to allow execution of server side commands

		java.util.List<String> helps = iChat.getCommandRegistry().getHelpForAllCommands();
		StringBuilder sb = new StringBuilder();

		sb.append(MessageFormat.format(Bundles.COMMAND.getResource().getString("help.side"),iChat.getIConnectionManager().getSide().toString().toLowerCase()));
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());


		for(String str : helps) {
			sb.append(str);
			sb.append(System.lineSeparator());
		}

			String str = sb.toString();
			ChatPacket cp = new ChatPacket(str,iChat.getLOCALActor().getActorName());
			iChat.getIConnectionManager().sendPackage(cp, iChat.getLOCALActor().getIIOHandler());

	}

	@Override
	public String[] helpCommand() {
		return new String[]{Bundles.COMMAND.getResource().getString("helptext.help")};
	}

	@Override
	public boolean isDebugModeOnly() {
		return false;
	}

}
