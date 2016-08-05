package bb.chat.command;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.command.WhisperPacket;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * @author BB20101997
 */

@SuppressWarnings("ClassNamingConvention")
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
		iChat.getBasicChatPanel().println(MessageFormat.format(ResourceBundle.getBundle(RES_NAME).getString("whisperSender"),com[1],com[2]));
	}

	@Override
	public String[] helpCommand() {

		//noinspection StringConcatenationMissingWhitespace
		return new String[]{"Usage :" + COMMAND_INIT_CHAR + "whisper <ToPlayer> <Message>"};
	}

}
