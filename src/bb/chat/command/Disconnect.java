package bb.chat.command;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.net.enums.Side;
import bb.net.packets.connecting.DisconnectPacket;

/**
 * @author BB20101997
 */
public class Disconnect implements ICommand {

	private static final String[] HELPMESSAGE = new String[]{"Disconnects the client from the Server,only executed Client Side"};

	@Override
	public String[] getAlias() {
		return new String[0];
	}

	@Override
	public String getName() {

		return "disconnect";
	}

	@Override
	public void runCommand(String commandLine, IChat iChat) {
		if(iChat.getIConnectionHandler().getSide() == Side.CLIENT) {
			iChat.getIConnectionHandler().sendPackage(new DisconnectPacket(), iChat.getIConnectionHandler().SERVER());
			iChat.getIConnectionHandler().disconnect(iChat.getLocalActor().getIIOHandler());
			iChat.getBasicChatPanel().WipeLog();
			iChat.getBasicChatPanel().println("Successfully disconnected!");
		}
	}

	@Override
	public String[] helpCommand() {

		return HELPMESSAGE;
	}

	@Override
	public boolean isDebugModeOnly() {
		return false;
	}

}
