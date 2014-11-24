package bb.chat.command;

import bb.chat.enums.Side;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IMessageHandler;
import bb.chat.network.packet.Chatting.ChatPacket;

/**
 * @author BB20101997
 */
public class Help implements ICommand {


	@Override
	public int maxParameterCount() {
		return -1;
	}

	@Override
	public int minParameterCount() {
		return 0;
	}

	@Override
	public String[] getAlias() {
		return new String[0];
	}

	@Override
	public String getName() {

		return "help";
	}

	@Override
	public boolean runCommand(String commandLine, IMessageHandler imh) {
		if(imh.getSide() == Side.CLIENT) {
			String[] helps = imh.getHelpForAllCommands();
			StringBuilder s = new StringBuilder();

			s.append("Help for the server side commands:");
			s.append("\n");

			for(String str : helps) {
				s.append(str);
				s.append("\n");
			}

			String str = s.toString();
			imh.setEmpfaenger(imh.getActor());
			imh.sendPackage(new ChatPacket(str, imh.getActor().getActorName()));
			System.out.println("Executing Help Command");
			return true;
		} else {
			String[] helps = imh.getHelpForAllCommands();
			StringBuilder s = new StringBuilder();

			s.append("Help for the client side commands:");
			s.append("\n");

			for(String str : helps) {
				s.append(str);
				s.append("\n");
			}

			String str = s.toString();
			imh.setEmpfaenger(imh.getActor());
			imh.sendPackage(new ChatPacket(str, imh.getActor().getActorName()));
			return false;
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
