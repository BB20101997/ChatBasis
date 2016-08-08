package bb.chat.command;

import bb.chat.enums.Bundles;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.handshake.SignUpPacket;

import java.text.MessageFormat;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class Register implements ICommand {

	@Override
	public String getName() {
		return "register";
	}

	@Override
	public void runCommand(String commandLine, IChat iChat) {
		String[] com = commandLine.split(" ", 4);
		if(com.length == 4) {
			if(com[2].equals(com[3])) {
				SignUpPacket p = new SignUpPacket();
				p.setPassword(com[2]);
				p.setUsername(com[1]);
				iChat.getIConnectionManager().sendPackage(p, iChat.getIConnectionManager().SERVER());
				iChat.getBasicChatPanel().println(MessageFormat.format(Bundles.MESSAGE.getResource().getString("register.send"), iChat.getIConnectionManager().getSide().toString().toUpperCase()));
			} else {
				iChat.getBasicChatPanel().println(MessageFormat.format(Bundles.MESSAGE.getResource().getString("register.mismatch"), iChat.getIConnectionManager().getSide().toString().toUpperCase()));
			}
		}
	}

}
