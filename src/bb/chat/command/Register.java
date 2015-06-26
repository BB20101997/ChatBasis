package bb.chat.command;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.handshake.SignUpPacket;
import bb.net.enums.Side;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class Register implements ICommand {

	@Override
	public String[] getAlias() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "register";
	}

	@Override
	public void runCommand(String commandLine, IChat iChat) {
		if(iChat.getIConnectionManager().getSide() == Side.CLIENT) {
			String[] c = commandLine.split(" ", 4);
			if(c.length == 4 && c[2].equals(c[3])) {
				SignUpPacket p = new SignUpPacket();
				p.setPassword(c[2]);
				p.setUsername(c[1]);
				iChat.getIConnectionManager().sendPackage(p, iChat.getIConnectionManager().SERVER());
			} else if(c.length == 4) {
				iChat.getBasicChatPanel().println("[Client] Password and Repeated Password did not match!");
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
