package bb.chat.network.packet.command;

import bb.chat.enums.QuerryType;
import bb.net.enums.NetworkState;
import bb.net.interfaces.APacket;
import bb.net.packets.DataIn;
import bb.net.packets.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 25.01.2015.
 */
public class QuerryPacket extends APacket {

	private QuerryType QT;

	private boolean request;
	private String response = "";

	public QuerryPacket() {
		minNetworkState = NetworkState.POST_HANDSHAKE;
	}

	public QuerryPacket(QuerryType qt) {
		QT = qt;
		request = true;
	}

	public QuerryPacket(QuerryType qt, String s) {
		QT = qt;
		request = false;
		response = s;
	}

	public QuerryType getQT() {
		return QT;
	}

	public String getResponse() {
		return response;
	}

	public boolean isRequest() {
		return request;
	}

	@Override
	public void writeToData(DataOut dataOut) throws IOException {
		dataOut.writeInt(QT.ordinal());
		dataOut.writeBoolean(request);
		dataOut.writeUTF(response);
	}

	@Override
	public void readFromData(DataIn dataIn) throws IOException {
		QT = QuerryType.values()[dataIn.readInt()];
		request = dataIn.readBoolean();
		response = dataIn.readUTF();
	}
}
