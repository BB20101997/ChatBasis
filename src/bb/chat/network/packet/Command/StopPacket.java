package bb.chat.network.packet.Command;

import bb.chat.interfaces.IPacket;
import bb.chat.network.packet.DataIn;
import bb.chat.network.packet.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class StopPacket extends IPacket {

	public StopPacket() {
	}

	@Override
	public void writeToData(DataOut dataOut) throws IOException {

	}

	@Override
	public void readFromData(DataIn dataIn) throws IOException {

	}
}
