package bb.chat.network.packet.Command;

import bb.chat.interfaces.APacket;
import bb.chat.network.packet.DataIn;
import bb.chat.network.packet.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 01.09.2014.
 */
public class WhisperPacket extends APacket {

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
