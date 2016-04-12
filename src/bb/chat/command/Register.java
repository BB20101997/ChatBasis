package bb.chat.command;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.handshake.SignUpPacket;

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
			if(com.length == 4 && com[2].equals(com[3])) {
				SignUpPacket p = new SignUpPacket();
				p.setPassword(com[2]);
				p.setUsername(com[1]);
				iChat.getIConnectionManager().sendPackage(p, iChat.getIConnectionManager().SERVER());
				iChat.getBasicChatPanel().println("["+iChat.getIConnectionManager().getSide().toString().toUpperCase()+"] Sending Register Request!");
			} else if(com.length == 4) {
				iChat.getBasicChatPanel().println("["+iChat.getIConnectionManager().getSide().toString().toUpperCase()+"] Password and Repeated Password did not match!");
			}
	}

}
