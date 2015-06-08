package bb.chat.command;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.chatting.ChatPacket;
import bb.net.enums.Side;

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
		if(iChat.getIConnectionHandler().getSide() == Side.CLIENT) {
			String[] helps = iChat.getCommandRegistry().getHelpForAllCommands();
			StringBuilder s = new StringBuilder();

			s.append("Help for the server side commands:");
			s.append(System.lineSeparator());

			for(String str : helps) {
				s.append(str);
				s.append(System.lineSeparator());
			}

			String str = s.toString();
			iChat.getIConnectionHandler().sendPackage(new ChatPacket(str, iChat.getLocalActor().getActorName()), iChat.getLocalActor().getIIOHandler());
		} else {
			String[] helps = iChat.getCommandRegistry().getHelpForAllCommands();
			StringBuilder s = new StringBuilder();

			s.append("Help for the client side commands:");
			s.append(System.lineSeparator());

			for(String str : helps) {
				s.append(str);
				s.append(System.lineSeparator());
			}

			String str = s.toString();
			iChat.getIConnectionHandler().sendPackage(new ChatPacket(str, iChat.getLocalActor().getActorName()), iChat.getLocalActor().getIIOHandler());
		}
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
