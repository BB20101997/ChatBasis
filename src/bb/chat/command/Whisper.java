package bb.chat.command;

import bb.chat.enums.Side;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IMessageHandler;
import bb.chat.network.packet.Command.WhisperPacket;

/**
 * @author BB20101997
 */

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
	public void runCommand(String commandLine, IMessageHandler imh) {

		String[] c = commandLine.split(" ", 3);

		if(imh.getSide() == Side.CLIENT) {
			if(c.length <= 2) {
				return;
			}
			imh.setEmpfaenger(IMessageHandler.SERVER);
			imh.sendPackage(new WhisperPacket(imh.getActor().getActorName(), c[2], c[1]));
		} else {
			String str[] = commandLine.split(" ", 3);
			if(str.length > 2) {
				System.out.println(str[1] + " : " + str[2]);
				imh.setEmpfaenger(imh.getUserByName(str[1]));
				imh.sendPackage(new WhisperPacket(imh.getActor().getActorName(), str[2], c[1]));
			}
		}
	}

	@Override
	public String[] helpCommand() {

		return new String[]{"Usage : /whisper <ToPlayer> <Message>"};
	}

	@Override
	public boolean debugModeOnly() {
		return false;
	}

}
