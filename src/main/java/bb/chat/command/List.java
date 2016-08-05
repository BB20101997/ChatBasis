package bb.chat.command;

import bb.chat.enums.QuerryType;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.command.QuerryPacket;
import bb.net.enums.Side;

import java.util.ResourceBundle;

/**
 * Created by BB20101997 on 25.01.2015.
 */
@SuppressWarnings("ClassNamingConvention")
public class List implements ICommand {
	@Override
	public String[] getAlias() {
		return new String[0];
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
		return new String[]{ResourceBundle.getBundle("bb.chat.lang.Command").getString("listHelp")};
	}

	@Override
	public boolean isDebugModeOnly() {
		return false;
	}
}
