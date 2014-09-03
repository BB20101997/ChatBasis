package bb.chat.network.packet.Chatting;

import bb.chat.interfaces.IPacket;
import bb.chat.network.packet.DataIn;
import bb.chat.network.packet.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class ChatPacket extends IPacket {

    public String message;
    public String Sender;

    public ChatPacket(String message, String Sender){
        this.message = message;
        this.Sender = Sender;
    }

    public IPacket newInstance(){
        return new ChatPacket("","");
    }

    @Override
    public void writeToData(DataOut dataOut) throws IOException{
        dataOut.writeUTF(message);
        dataOut.writeUTF(Sender);
    }

    @Override
    public void readFromData(DataIn dataIn) throws IOException{
        message = dataIn.readUTF();
        Sender = dataIn.readUTF();
    }
}
