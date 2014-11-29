package bb.chat.network.packet.Command;

import bb.chat.interfaces.IPacket;
import bb.chat.network.packet.DataIn;
import bb.chat.network.packet.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class RenamePacket extends IPacket {

	public String newName;
	public String oldName;

	public RenamePacket() {
		newName = "";
		oldName = "";
	}

	public RenamePacket(String old, String newN) {
		oldName = old;
		newName = newN;
	}

	@Override
	public void writeToData(DataOut dataOut) throws IOException {
		dataOut.writeUTF(newName);
		dataOut.writeUTF(oldName);
	}

	@Override
	public void readFromData(DataIn dataIn) throws IOException {
		newName = dataIn.readUTF();
		oldName = dataIn.readUTF();
	}
}
