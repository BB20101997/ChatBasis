package bb.chat.network.packet;

import bb.chat.interfaces.IPacket;

import java.io.IOException;

/**
 * Created by BB20101997 on 28.11.2014.
 */
public class PermissionPacket extends IPacket {

	public String cmd;
	public String user;
	public String perm;

	public PermissionPacket() {}

	public PermissionPacket(String commandType, String userToChange, String permission) {
		cmd = commandType;
		user = userToChange;
		perm = permission;
	}

	@Override
	public void writeToData(DataOut dataOut) throws IOException {
		dataOut.writeUTF(cmd);
		dataOut.writeUTF(user);
		dataOut.writeUTF(perm);
	}

	@Override
	public void readFromData(DataIn dataIn) throws IOException {
		cmd = dataIn.readUTF();
		user = dataIn.readUTF();
		perm = dataIn.readUTF();
	}
}
