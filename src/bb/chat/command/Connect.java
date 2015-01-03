package bb.chat.command;

import bb.chat.enums.Side;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IMessageHandler;

/**
 * @author BB20101997
 */
public class Connect implements ICommand {

	@Override
	public String[] getAlias() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "connect";
	}

	@Override
	public void runCommand(String commandLine, IMessageHandler imh) {
		if(imh.getSide() == Side.CLIENT) {
			String[] strA = commandLine.split(" ");
			imh.wipe();
			if(strA.length >= 3) {
				imh.connect(strA[1], Integer.valueOf(strA[2]));
			} else if(strA.length >= 2) {
				imh.connect(strA[1], 256);
			} else {
				imh.connect("192.168.178.21", 256);
			}
		}
	}

	@Override
	public String[] helpCommand() {
		return new String[0];
	}

	@Override
	public boolean debugModeOnly() {
		return false;
	}
}
