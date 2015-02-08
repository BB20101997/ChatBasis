package bb.chat.command;

import bb.chat.enums.Side;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IConnectionHandler;
import bb.chat.network.packet.Handshake.SignUpPacket;

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
	public void runCommand(String commandLine, IConnectionHandler imh) {
		if(imh.getSide() == Side.CLIENT) {
			String[] c = commandLine.split(" ", 4);
			if(c.length == 4 && c[2].equals(c[3])) {
				SignUpPacket p = new SignUpPacket();
				p.setPassword(c[2]);
				p.setUsername(c[1]);
				imh.sendPackage(p, IConnectionHandler.SERVER);
			} else if(c.length == 4) {
				imh.println("[Client] Password and Repeated Password did not match!");
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
