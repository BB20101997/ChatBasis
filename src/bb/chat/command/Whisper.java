package bb.chat.command;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.command.WhisperPacket;
import bb.net.enums.Side;

/**
 * @author BB20101997
 */

@SuppressWarnings("HardcodedFileSeparator")
public class Whisper implements ICommand {

	@Override
	public String[] getAlias() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "whisper";
	}

	@Override
	public void runCommand(String commandLine, IChat iChat) {

		String[] c = commandLine.split(" ", 3);

		if(iChat.getIConnectionHandler().getSide() == Side.CLIENT) {
			if(c.length <= 2) {
				return;
			}
			iChat.getIConnectionHandler().sendPackage(new WhisperPacket(iChat.getLocalActor().getActorName(), c[2], c[1]), iChat.getIConnectionHandler().SERVER());
		} else {
			String str[] = commandLine.split(" ", 3);
			if(str.length > 2) {
				iChat.getIConnectionHandler().sendPackage(new WhisperPacket(iChat.getLocalActor().getActorName(), str[2], c[1]), iChat.getActorByName(str[1]).getIIOHandler());
			}
		}
	}

	@Override
	public String[] helpCommand() {

		//noinspection StringConcatenationMissingWhitespace
		return new String[]{"Usage :"+ICommand.COMMAND_INIT_CHAR +"whisper <ToPlayer> <Message>"};
	}

	@Override
	public boolean isDebugModeOnly() {
		return false;
	}

}
