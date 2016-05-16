package bb.chat.command;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.chatting.ChatPacket;

/**
 * @author BB20101997
 */
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
		//TODO: add a possibility to get server side commands on the client, also needs changes to the command registry to allow execution of server side commands

		String[] helps = iChat.getCommandRegistry().getHelpForAllCommands();
		StringBuilder sb = new StringBuilder();

		sb.append("Help for the ");
		sb.append(iChat.getIConnectionManager().getSide().toString().toLowerCase());
		sb.append(" side commands:");
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

		return new String[]{"This will Display the help messages!"};
	}

	@Override
	public boolean isDebugModeOnly() {
		return false;
	}

}
