package bb.chat.network.packet.handshake;

import bb.net.interfaces.APacket;
import bb.net.packets.DataIn;
import bb.net.packets.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 19.02.2015.
 */
@SuppressWarnings("ClassIndependentOfModule")
public class GuestPacket extends APacket {

	public int number = 0;

	@Override
	public void writeToData(DataOut dataOut) throws IOException {
		dataOut.writeInt(number);
	}

	@Override
	public void readFromData(DataIn dataIn) throws IOException {
		number = dataIn.readInt();
	}
}
