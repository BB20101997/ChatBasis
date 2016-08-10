package bb.chat.command;

import bb.chat.enums.Bundles;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.chatting.ChatPacket;

import java.text.MessageFormat;

/**
 * @author BB20101997
 */
@SuppressWarnings("ClassNamingConvention")
public class Help implements ICommand {

	//TODO:FIX runCommand

	@Override
	public String[] getAlias() {
		return new String[0];
	}

	@Override
	public String getName() {

		return "help";
	}

	@Override
	public void runCommand(String commandLine, IChat iChat) {

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