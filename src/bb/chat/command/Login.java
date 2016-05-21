package bb.chat.command;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.handshake.LoginPacket;
import bb.net.enums.Side;

/**
 * Created by BB20101997 on 30.08.2014.
 */
@SuppressWarnings("ClassNamingConvention")
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
		if(iChat.getIConnectionManager().getSide() == Side.CLIENT) {
			String[] com = commandLine.split(" ", 3);
			if(com.length == 3) {
				LoginPacket p = new LoginPacket();
				p.setPassword(com[2]);
				p.setUsername(com[1]);
				iChat.getIConnectionManager().sendPackage(p, iChat.getIConnectionManager().SERVER());
			}
		}
	}

	@Override
	public boolean isDebugModeOnly() {
		return false;
	}
}
