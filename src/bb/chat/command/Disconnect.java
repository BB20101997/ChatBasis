package bb.chat.command;

import bb.chat.enums.Side;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IMessageHandler;
import bb.chat.network.packet.Command.DisconnectPacket;

/**
 * @author BB20101997
 */
public class Disconnect implements ICommand {

	private static final String[] helpMessage = new String[]{"Disconnects the client from the Server,only executed Client Side"};


	@Override
	public int maxParameterCount() {
		return 0;
	}

	@Override
	public int minParameterCount() {
		return 0;
	}

	@Override
	public String[] getAlias() {
		return new String[0];
	}

	@Override
	public String getName() {

		return "disconnect";
	}

	@Override
	public boolean runCommand(String commandLine, IMessageHandler imh) {
		if(imh.getSide() == Side.CLIENT) {
			imh.setEmpfaenger(IMessageHandler.SERVER);
			imh.sendPackage(new DisconnectPacket());
			imh.disconnect(imh.getActor());
			imh.wipe();
			imh.println("Successfully disconnected!");

		}
		return true;
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
