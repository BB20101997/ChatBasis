package bb.chat.command;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.handshake.LoginPacket;
import bb.net.enums.Side;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class Login implements ICommand {

	@Override
	public String[] getAlias() {
		return new String[0];
	}

	public String getName() {
		return "login";
	}

	@Override
	public void runCommand(String commandLine, IChat iChat) {
		if(iChat.getIConnectionHandler().getSide() == Side.CLIENT) {
			String[] c = commandLine.split(" ", 3);
			if(c.length == 3) {
				LoginPacket p = new LoginPacket();
				p.setPassword(c[2]);
				p.setUsername(c[1]);
				iChat.getIConnectionHandler().sendPackage(p, iChat.getIConnectionHandler().SERVER());
			}
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
