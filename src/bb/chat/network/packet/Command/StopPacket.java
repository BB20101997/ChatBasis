package bb.chat.network.packet.command;

import bb.net.interfaces.APacket;
import bb.net.packets.DataIn;
import bb.net.packets.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class StopPacket extends APacket {

	public StopPacket() {
	}

	@Override
	public void writeToData(DataOut dataOut) throws IOException {

	}

	@Override
	public void readFromData(DataIn dataIn) throws IOException {

	}
}
