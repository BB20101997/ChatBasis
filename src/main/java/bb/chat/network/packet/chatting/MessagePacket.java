package bb.chat.network.packet.chatting;

import bb.net.interfaces.APacket;
import bb.net.packets.DataIn;
import bb.net.packets.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 08. Aug. 2016.
 */
public class MessagePacket extends APacket {


	public String stringKey;
	public String[] stringArgs = new String[0];

	@SuppressWarnings("unused")
	public MessagePacket(){}

	public MessagePacket(String key,String ... args){
		stringKey = key;
		stringArgs = args;
	}

	@Override
	public void writeToData(DataOut dataOut) throws IOException {
		dataOut.writeUTF(stringKey);
		dataOut.writeInt(stringArgs.length);
		for(String s:stringArgs){
			dataOut.writeUTF(s);
		}
	}

	@Override
	public void readFromData(DataIn dataIn) throws IOException {
		stringKey = dataIn.readUTF();
		int length = dataIn.readInt();
		stringArgs = new String[length];
		for(int i = 0;i<length;i++){
			stringArgs[i] = dataIn.readUTF();
		}
	}
}
