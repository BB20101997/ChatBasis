package bb.chat.network.packet.Command;

import bb.chat.interfaces.IPacket;
import bb.chat.network.packet.DataIn;
import bb.chat.network.packet.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 01.09.2014.
 */
public class WhisperPacket extends IPacket {

    public String message;
    public String sender;
    public String receiver;

    public WhisperPacket(){}


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
