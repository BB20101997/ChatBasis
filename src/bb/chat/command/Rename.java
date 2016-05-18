package bb.chat.command;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.IChatActor;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.command.RenamePacket;
import bb.net.enums.Side;

/**
 * @author BB20101997
 */
@SuppressWarnings("HardcodedFileSeparator")
public class Rename implements ICommand {

	@Override
	public String getName() {

		return "rename";
	}

	@Override
	public void runCommand(final String commandLine,final IChat iChat) {
		String[] dS = commandLine.split(" ");
		//Client, SERVER and ALL are reserved names
		if(dS.length <= 2 || "Client".equals(dS[2]) || "SERVER".equals(dS[2]) || "ALL".equals(dS[2])) {
			return;
		}
		if(iChat.getIConnectionManager().getSide() == Side.CLIENT) {
			//if client just inform Server of the request
			iChat.getIConnectionManager().sendPackage(new RenamePacket(dS[1], dS[2]), iChat.getIConnectionManager().ALL());
		} else {
			//get the players actor
			IChatActor ica = iChat.getActorByName(dS[1]);
			if(ica != null) {
				ica.setActorName(dS[2],true);
				iChat.getBasicChatPanel().println("[" + iChat.getLOCALActor().getActorName() + "] " + dS[1] + " is now known as " + dS[2]);
				//iChat.getIConnectionManager().sendPackage(new RenamePacket(dS[1], dS[2]), iChat.getIConnectionManager().ALL());
			}
		}
	}

	@Override
	public String[] helpCommand() {
		//noinspection StringConcatenationMissingWhitespace
		return new String[]{ICommand.COMMAND_INIT_CHAR +"rename <new Name>", "Used to rename you in Chat!"};
	}
}
