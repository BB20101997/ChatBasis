package bb.chat.network.packet.command;

import bb.net.interfaces.APacket;
import bb.net.packets.DataIn;
import bb.net.packets.DataOut;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.io.IOException;
import java.util.logging.Logger;

import static bb.chat.basis.BasisConstants.LOG_NAME;

/**
 * Created by BB20101997 on 01.09.2014.
 */
public class WhisperPacket extends APacket {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(WhisperPacket.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}

	public String getMessage() {
		return message;
	}

	public String getSender() {
		return sender;
	}

	public String getReceiver() {
		return receiver;

	}

	private String message;
	private String sender;
	private String receiver;

	public WhisperPacket() {
	}

	public WhisperPacket(String sender, String message, String empfaenger) {
		log.fine("Sending a Message from "+sender+" to "+empfaenger+"!");
		this.message = message;
		this.sender = sender;
		receiver = empfaenger;
	}


	@Override
	public void writeToData(DataOut dataOut) throws IOException {
		dataOut.writeUTF(message);
		dataOut.writeUTF(sender);
		dataOut.writeUTF(receiver);
	}

	@Override
	public void readFromData(DataIn dataIn) throws IOException {
		message = dataIn.readUTF();
		sender = dataIn.readUTF();
		receiver = dataIn.readUTF();
	}
}
