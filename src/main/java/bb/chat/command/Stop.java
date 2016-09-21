package bb.chat.command;

import bb.chat.enums.Bundles;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.command.StopPacket;
import bb.net.enums.Side;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.util.logging.Logger;

import static bb.chat.basis.BasisConstants.LOG_NAME;

/**
 * Created by BB20101997 on 30.08.2014.
 */
@SuppressWarnings("ClassNamingConvention")
public class Stop implements ICommand {
	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(Stop.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}

	@Override
	public String[] getAlias() {
		return new String[]{Bundles.COMMAND.getString("alias.1.stop")};
	}

	@Override
	public String getName() {
		return Bundles.COMMAND.getString("name.stop");
	}

	@Override
	public void runCommand(String commandLine, IChat iChat) {
		log.fine("Executing stop ... shutdown may follow!");
		if(iChat.getIConnectionManager().getSide() == Side.CLIENT) {
			iChat.getIConnectionManager().sendPackage(new StopPacket(), iChat.getIConnectionManager().SERVER());
		} else {
			iChat.shutdown();
		}
	}

}
