package bb.chat.network.packet.command;

import bb.chat.enums.QuerryType;
import bb.net.interfaces.APacket;
import bb.net.packets.DataIn;
import bb.net.packets.DataOut;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.io.IOException;
import java.util.logging.Logger;

import static bb.chat.basis.BasisConstants.LOG_NAME;


/**
 * Created by BB20101997 on 25.01.2015.
 */
public class QuerryPacket extends APacket {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(QuerryPacket.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}

	private QuerryType QT;

	private boolean request;
	private int requestID;
	private String response = "";

	public QuerryPacket() {
		this(QuerryType.NOT_DEFINED,"",false);
	}

	@SuppressWarnings("SameParameterValue")
	public QuerryPacket(QuerryType qt) {
		this(qt,"",true);
	}

	public QuerryPacket(QuerryType qt, String s) {
		this(qt,s,false);
	}

	public QuerryPacket(QuerryType querryType, String s, boolean req) {
		this(querryType,s,req,-1);
	}

	public QuerryPacket(QuerryType querryType, String s, boolean req,int id) {
		QT = querryType;
		response = s;
		request = req;
		requestID = id;
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
		dataOut.writeInt(requestID);
	}

	@Override
	public void readFromData(DataIn dataIn) throws IOException {
		QT = QuerryType.values()[dataIn.readInt()];
		request = dataIn.readBoolean();
		response = dataIn.readUTF();
		requestID = dataIn.readInt();
	}
}
