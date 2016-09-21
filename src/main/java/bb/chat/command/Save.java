package bb.chat.command;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.command.SavePacket;
import bb.net.enums.Side;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.util.logging.Logger;

import static bb.chat.basis.BasisConstants.LOG_NAME;

/**
 * Created by BB20101997 on 24.11.2014.
 */
@SuppressWarnings("ClassNamingConvention")
public class Save implements ICommand {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(Save.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}

	@Override
	public String getName() {
		return "save";
	}

	@Override
	public void runCommand(String commandLine, IChat iChat) {
		log.fine("Running save command!");
		if(iChat.getIConnectionManager().getSide() == Side.CLIENT) {
			iChat.getIConnectionManager().sendPackage(new SavePacket(), iChat.getIConnectionManager().SERVER());
		} else {
			iChat.save();
		}
	}

}
