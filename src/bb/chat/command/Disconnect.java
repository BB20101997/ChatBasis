package bb.chat.command;

import bb.chat.enums.Side;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IConnectionHandler;
import bb.chat.network.packet.Command.DisconnectPacket;

/**
 * @author BB20101997
 */
public class Disconnect implements ICommand {

	private static final String[] helpMessage = new String[]{"Disconnects the client from the Server,only executed Client Side"};

	@Override
	public String[] getAlias() {
		return new String[0];
	}

	@Override
	public String getName() {

		return "disconnect";
	}

	@Override
	public void runCommand(String commandLine, IConnectionHandler imh) {
		if(imh.getSide() == Side.CLIENT) {
			imh.sendPackage(new DisconnectPacket(), IConnectionHandler.SERVER);
			imh.disconnect(imh.getActor());
			imh.wipe();
			imh.println("Successfully disconnected!");
		}
	}

	@Override
	public String[] helpCommand() {

		return helpMessage;
	}

	@Override
	public boolean debugModeOnly() {
		return false;
	}

}
