package bb.chat.network.packet.command;

import bb.net.interfaces.APacket;
import bb.net.packets.DataIn;
import bb.net.packets.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 28.11.2014.
 */
public class PermissionPacket extends APacket {
//TODO just send complet command
	public String cmd;
	public String restCmd;

	public PermissionPacket() {}

	public PermissionPacket(String commandType, String restOfCommand) {
		cmd = commandType;
		restCmd = restOfCommand;
	}

	@Override
	public void writeToData(DataOut dataOut) throws IOException {
		dataOut.writeUTF(cmd);
		dataOut.writeUTF(restCmd);
	}

	@Override
	public void readFromData(DataIn dataIn) throws IOException {
		cmd = dataIn.readUTF();
		restCmd = dataIn.readUTF();
	}
}
