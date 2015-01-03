package bb.chat.network.packet.Command;

import bb.chat.interfaces.IPacket;
import bb.chat.network.packet.DataIn;
import bb.chat.network.packet.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 28.11.2014.
 */
public class PermissionPacket extends IPacket {

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
