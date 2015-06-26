package bb.chat.command;

import bb.chat.chat.ChatActor;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.chatting.ChatPacket;
import bb.chat.network.packet.command.RenamePacket;
import bb.net.enums.Side;

/**
 * @author BB20101997
 */
@SuppressWarnings("HardcodedFileSeparator")
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
	public void runCommand(String commandLine, IChat iChat) {
		String[] dS = commandLine.split(" ");
		if(dS.length <= 2 || "Client".equals(dS[2]) || "SERVER".equals(dS[2])) {
			return;
		}
		if(iChat.getIConnectionManager().getSide() == Side.CLIENT) {
			iChat.getIConnectionManager().sendPackage(new RenamePacket(dS[1], dS[2]), iChat.getIConnectionManager().ALL());
		} else {
			ChatActor ica = iChat.getActorByName(dS[1]);
			if(ica != null) {
				ica.setActorName(dS[2]);
				iChat.getBasicChatPanel().println("[" + iChat.getLocalActor().getActorName() + "] " + dS[1] + " is now known as " + dS[2]);
				iChat.getIConnectionManager().sendPackage(new ChatPacket(dS[1] + " is now known as " + dS[2], iChat.getLocalActor().getActorName()), iChat.getIConnectionManager().ALL());
				iChat.getIConnectionManager().sendPackage(new RenamePacket(dS[1], dS[2]), iChat.getActorByName(dS[2]).getIIOHandler());
			}
		}
	}

	@Override
	public String[] helpCommand() {
		//noinspection StringConcatenationMissingWhitespace
		return new String[]{ICommand.COMMAND_INIT_CHAR +"rename <new Name>", "Used to rename you in Chat!"};
	}

	@Override
	public boolean isDebugModeOnly() {
		return false;
	}

}
