package bb.chat.command;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.net.enums.Side;

/**
 * @author BB20101997
 */
public class Connect implements ICommand {

	@Override
	public String getName() {
		return "connect";
	}

	@Override
	public void runCommand(String commandLine, IChat iChat) {
		if(iChat.getIConnectionManager().getSide() == Side.CLIENT) {
			String[] strA = commandLine.split(" ");
			iChat.getBasicChatPanel().WipeLog();
			if(strA.length >= 3) {
				iChat.getIConnectionManager().connect(strA[1], Integer.valueOf(strA[2]));
			} else if(strA.length >= 2) {
				iChat.getIConnectionManager().connect(strA[1], 256);
			} else {
				iChat.getIConnectionManager().connect("192.168.178.21", 256);
			}
		}
	}

	@Override
	public String[] helpCommand() {
		//noinspection StringConcatenationMissingWhitespace
		return new String[]{"Usage:"+COMMAND_INIT_STRING+"connect <host> <port>"};
	}
}
