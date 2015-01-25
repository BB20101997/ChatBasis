package bb.chat.network.packet.Handshake;

import bb.chat.enums.NetworkState;
import bb.chat.interfaces.IPacket;
import bb.chat.network.packet.DataIn;
import bb.chat.network.packet.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 31.08.2014.
 */
public class HandshakePacket extends IPacket {

	private String Version = "1.0";

	public HandshakePacket(){
		minNetworkState = NetworkState.UNKNOWN;
	}

	@Override
	public void writeToData(DataOut dataOut) throws IOException {
		dataOut.writeUTF(Version);
	}

	@Override
	public void readFromData(DataIn dataIn) throws IOException {
		Version = dataIn.readUTF();
	}
}
