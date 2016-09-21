package bb.chat.command;

import bb.chat.enums.Bundles;
import bb.chat.enums.QuerryType;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.command.QuerryPacket;
import bb.net.enums.Side;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.util.logging.Logger;

import static bb.chat.basis.BasisConstants.LOG_NAME;

/**
 * Created by BB20101997 on 25.01.2015.
 */
@SuppressWarnings("ClassNamingConvention")
public class List implements ICommand {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(List.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}

	@Override
	public String getName() {
		return "list";
	}

	@Override
	public void runCommand(String commandLine, IChat iChat) {
		if(iChat.getIConnectionManager().getSide() == Side.SERVER) {
			String[] names = iChat.getActiveUserList();
			for(String name : names) {
				iChat.getBasicChatPanel().println(name);
			}
		} else {
			iChat.getIConnectionManager().sendPackage(new QuerryPacket(QuerryType.ONLINEUSERSLIST), iChat.getIConnectionManager().SERVER());
		}
	}

	@Override
	public String[] helpCommand() {
		return new String[]{Bundles.COMMAND.getResource().getString("helptext.list")};
	}

}
