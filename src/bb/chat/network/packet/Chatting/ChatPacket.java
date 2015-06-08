package bb.chat.network.packet.chatting;

import bb.net.interfaces.APacket;
import bb.net.packets.DataIn;
import bb.net.packets.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class ChatPacket extends APacket {

	public String message;
	public String Sender;

	public ChatPacket(String message, String Sender) {
		this.message = message;
		this.Sender = Sender;
	}

	public ChatPacket() {
		message = "";
		Sender = "";
	}

	@Override
	public void writeToData(DataOut dataOut) throws IOException {
		dataOut.writeUTF(message);
		dataOut.writeUTF(Sender);
	}

	@Override
	public void readFromData(DataIn dataIn) throws IOException {
		message = dataIn.readUTF();
		Sender = dataIn.readUTF();
	}
}
