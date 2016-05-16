package bb.chat.command;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.command.WhisperPacket;

/**
 * @author BB20101997
 */

@SuppressWarnings("HardcodedFileSeparator")
public class Whisper implements ICommand {

	@Override
	public String[] getAlias() {
		return new String[]{"w"};
	}

	@Override
	public String getName() {
		return "whisper";
	}

	@Override
	public void runCommand(String commandLine, IChat iChat) {
		String[] com = commandLine.split(" ", 3);
		if(com.length <= 2) {
			return;
		}
		iChat.getIConnectionManager().sendPackage(new WhisperPacket(iChat.getLOCALActor().getActorName(), com[2], com[1]), iChat.getIConnectionManager().SERVER());
	}

	@Override
	public String[] helpCommand() {

		//noinspection StringConcatenationMissingWhitespace
		return new String[]{"Usage :" + ICommand.COMMAND_INIT_CHAR + "whisper <ToPlayer> <Message>"};
	}

}
