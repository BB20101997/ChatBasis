package bb.chat.network.packet.chatting;

import bb.net.interfaces.APacket;
import bb.net.packets.DataIn;
import bb.net.packets.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class ChatPacket extends APacket {

	public String Message = "on the sending end!";
	public String Sender = "Something went wrong";

	@SuppressWarnings("unused")
	public ChatPacket() {
		//it is used!
	}

	public ChatPacket(String message, String sender) {
		Message = message;
		Sender = sender;
	}
	
	@Override
	public void writeToData(DataOut dataOut) throws IOException {
		dataOut.writeUTF(Message);
		dataOut.writeUTF(Sender);
	}

	@Override
	public void readFromData(DataIn dataIn) throws IOException {
		Message = dataIn.readUTF();
		Sender = dataIn.readUTF();
	}
}
