package bb.chat.command;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.command.SavePacket;
import bb.net.enums.Side;

/**
 * Created by BB20101997 on 24.11.2014.
 */
public class Save implements ICommand {

	@Override
	public String[] getAlias() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "save";
	}

	@Override
	public void runCommand(String commandLine, IChat iChat) {
		if(iChat.getIConnectionHandler().getSide() == Side.CLIENT) {
			iChat.getIConnectionHandler().sendPackage(new SavePacket(), iChat.getIConnectionHandler().SERVER());
		} else {
			iChat.save();
		}
	}

	@Override
	public String[] helpCommand() {
		return new String[0];
	}

	@Override
	public boolean isDebugModeOnly() {
		return false;
	}
}
