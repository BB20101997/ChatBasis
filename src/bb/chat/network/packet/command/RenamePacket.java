package bb.chat.network.packet.command;

import bb.net.interfaces.APacket;
import bb.net.packets.DataIn;
import bb.net.packets.DataOut;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class RenamePacket extends APacket {

	public String newName;
	public String oldName;

	public RenamePacket() {
		this("","");
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

	@Override
	public String toString() {
		return MessageFormat.format("[RenamePacket] Renaming from {0} to {1}", oldName, newName);
	}
}
