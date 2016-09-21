package bb.chat.network.packet.chatting;

import bb.net.interfaces.APacket;
import bb.net.packets.DataIn;
import bb.net.packets.DataOut;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Logger;

import static bb.chat.basis.BasisConstants.LOG_NAME;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class ChatPacket extends APacket {

	@SuppressWarnings("HardCodedStringLiteral")
	public String Message = "on the sending end!";
	@SuppressWarnings("HardCodedStringLiteral")
	public String Sender  = "Something went wrong";

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(ChatPacket.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}

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

	@Override
	public String toString() {
		return MessageFormat.format("[ChatPacket]Sender='{'{1}'}',Message='{'{0}'}'", Message, Sender);
	}
}
