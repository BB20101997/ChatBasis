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
 * Created by BB20101997 on 28.11.2014.
 */
public class PermissionPacket extends APacket {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(PermissionPacket.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}
	
	private String completeCommand;

	@SuppressWarnings("unused")
	public PermissionPacket() {}

	public PermissionPacket(String command){
		completeCommand = command;
	}

	public String getCommand(){
		return completeCommand;
	}

	@Override
	public void writeToData(DataOut dataOut) throws IOException {
		dataOut.writeUTF(completeCommand);
	}

	@Override
	public void readFromData(DataIn dataIn) throws IOException {
		completeCommand = dataIn.readUTF();
	}
}
