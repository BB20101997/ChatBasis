package bb.chat.command;

import bb.chat.enums.Side;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IIOHandler;
import bb.chat.interfaces.IMessageHandler;
import bb.chat.network.packet.Chatting.ChatPacket;
import bb.chat.network.packet.Command.RenamePacket;

/**
 * @author BB20101997
 */
public class Rename implements ICommand {

	@Override
	public String[] getAlias() {
		return new String[0];
	}

	@Override
	public String getName() {

		return "rename";
	}

	@Override
	public void runCommand(String commandLine, IMessageHandler imh) {
		String[] dS = commandLine.split(" ");
		if(dS.length <= 2 || "Client".equals(dS[2]) || "SERVER".equals(dS[2])) {
			return;
		}
		if(imh.getSide() == Side.CLIENT) {
			imh.setEmpfaenger(IMessageHandler.ALL);
			imh.sendPackage(new RenamePacket(dS[1], dS[2]));
		} else {
			IIOHandler ica = imh.getUserByName(dS[1]);
			if(ica != null) {
				ica.setActorName(dS[2]);
				imh.println("[" + imh.getActor().getActorName() + "] " + dS[1] + " is now known as " + dS[2]);
				imh.setEmpfaenger(IMessageHandler.ALL);
				imh.sendPackage(new ChatPacket(dS[1] + " is now known as " + dS[2], imh.getActor().getActorName()));
				imh.setEmpfaenger(imh.getUserByName(dS[2]));
				imh.sendPackage(new RenamePacket(dS[1], dS[2]));
			}
		}
	}

	@Override
	public String[] helpCommand() {

		return new String[]{"/rename <new Name>", "Used to rename you in Chat!"};
	}

	@Override
	public boolean debugModeOnly() {
		return false;
	}

}
