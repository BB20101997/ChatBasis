package bb.chat.command;

import bb.chat.enums.Bundles;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.handshake.SignUpPacket;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.text.MessageFormat;
import java.util.logging.Logger;

import static bb.chat.basis.BasisConstants.LOG_NAME;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class Register implements ICommand {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(Register.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}

	@Override
	public String getName() {
		return "register";
	}

	@Override
	public void runCommand(String commandLine, IChat iChat) {
		log.fine("Trying to register new User!");
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
