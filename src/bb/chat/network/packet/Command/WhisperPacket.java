package bb.chat.network.packet.Command;

import bb.chat.interfaces.IPacket;
import bb.chat.network.packet.DataIn;
import bb.chat.network.packet.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 01.09.2014.
 */
public class WhisperPacket extends IPacket {

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {

		this.message = message;
		hasData();
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
		hasData();
	}

	public String getReceiver() {
		return receiver;

	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
		hasData();
	}

	protected void hasData() {
		boolean b;
		b = !"".equals(message);
		b &= !"".equals(sender);
		b &= !"".equals(receiver);
		state = b ? PacketState.DATA : PacketState.EMPTY;
	}

	protected String message;
	protected String sender;
	protected String receiver;

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
		hasData();
	}
}
