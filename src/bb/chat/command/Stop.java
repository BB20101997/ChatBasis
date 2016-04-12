package bb.chat.command;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.command.StopPacket;
import bb.net.enums.Side;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class Stop implements ICommand {

	@Override
	public String[] getAlias() {
		return new String[]{"stopp"};
	}

	@Override
	public String getName() {
		return "stop";
	}

	@Override
	public void runCommand(String commandLine, IChat iChat) {
		if(iChat.getIConnectionManager().getSide() == Side.CLIENT) {
			iChat.getIConnectionManager().sendPackage(new StopPacket(), iChat.getIConnectionManager().SERVER());
		} else {
			iChat.shutdown();
		}
	}

}
