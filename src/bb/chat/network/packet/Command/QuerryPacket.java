package bb.chat.network.packet.Command;

import bb.chat.enums.NetworkState;
import bb.chat.enums.QuerryType;
import bb.chat.interfaces.IPacket;
import bb.chat.network.packet.DataIn;
import bb.chat.network.packet.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 25.01.2015.
 */
public class QuerryPacket extends IPacket {

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
