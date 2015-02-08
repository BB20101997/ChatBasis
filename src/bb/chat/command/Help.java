package bb.chat.command;

import bb.chat.enums.Side;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IConnectionHandler;
import bb.chat.network.packet.Chatting.ChatPacket;

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
	public void runCommand(String commandLine, IConnectionHandler imh) {
		if(imh.getSide() == Side.CLIENT) {
			String[] helps = imh.getIChatInstance().getCommandRegestry().getHelpForAllCommands();
			StringBuilder s = new StringBuilder();

			s.append("Help for the server side commands:");
			s.append("\n");

			for(String str : helps) {
				s.append(str);
				s.append("\n");
			}

			String str = s.toString();
			imh.sendPackage(new ChatPacket(str, imh.getActor().getActorName()),imh.getActor());
			System.out.println("Executing Help Command");
		} else {
			String[] helps = imh.getIChatInstance().getCommandRegestry().getHelpForAllCommands();
			StringBuilder s = new StringBuilder();

			s.append("Help for the client side commands:");
			s.append("\n");

			for(String str : helps) {
				s.append(str);
				s.append("\n");
			}

			String str = s.toString();
			imh.sendPackage(new ChatPacket(str, imh.getActor().getActorName()),imh.getActor());
		}
	}

	@Override
	public String[] helpCommand() {

		return new String[]{"This will Display the help messages!"};
	}

	@Override
	public boolean debugModeOnly() {
		return false;
	}

}
