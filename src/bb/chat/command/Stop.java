package bb.chat.command;

import bb.chat.enums.Side;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IConnectionHandler;
import bb.chat.network.packet.Command.StopPacket;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class Stop implements ICommand {

	@Override
	public String[] getAlias() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "stop";
	}

	@Override
	public void runCommand(String commandLine, IConnectionHandler imh) {
		if(imh.getSide() == Side.CLIENT) {
			imh.sendPackage(new StopPacket(), IConnectionHandler.SERVER);
		} else {
			imh.getIChatInstance().shutdown();
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
