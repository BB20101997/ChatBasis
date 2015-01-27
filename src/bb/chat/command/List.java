package bb.chat.command;

import bb.chat.enums.QuerryType;
import bb.chat.enums.Side;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IMessageHandler;
import bb.chat.network.packet.Command.QuerryPacket;

/**
 * Created by BB20101997 on 25.01.2015.
 */
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
	public void runCommand(String commandLine, IMessageHandler imh) {
		if(imh.getSide() == Side.SERVER) {
				String[] names = imh.getActiveUserList();
				for(String name : names) {
					imh.println(name);
			}
		}
		else{
			imh.sendPackage(new QuerryPacket(QuerryType.ONLINEUSERSLIST),IMessageHandler.SERVER);
		}
	}

	@Override
	public String[] helpCommand() {
		return new String[]{"Displays all online Users!"};
	}

	@Override
	public boolean debugModeOnly() {
		return false;
	}
}
