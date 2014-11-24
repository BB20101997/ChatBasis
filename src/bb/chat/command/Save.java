package bb.chat.command;

import bb.chat.enums.Side;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IMessageHandler;
import bb.chat.network.packet.Command.SavePacket;

/**
 * Created by BB20101997 on 24.11.2014.
 */
public class Save implements ICommand {


	@Override
	public int maxParameterCount() {
		return 0;
	}

	@Override
	public int minParameterCount() {
		return 0;
	}

	@Override
	public String[] getAlias() {
		return new String[0];
	}

	@Override
	public String getName() {
		return "save";
	}

	@Override
	public boolean runCommand(String commandLine, IMessageHandler imh) {
		if(imh.getSide()== Side.CLIENT){
			imh.sendPackage(new SavePacket());
		}
		else{
			imh.save();
		}

		return true;
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