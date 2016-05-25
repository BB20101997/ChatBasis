package bb.chat.network.packet.command;

import bb.net.interfaces.APacket;
import bb.net.packets.DataIn;
import bb.net.packets.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 28.11.2014.
 */
public class PermissionPacket extends APacket {
	//TODO remove partial commands
	private String completeCommand;

	public PermissionPacket() {}

	@Deprecated
	public PermissionPacket(final String commandType,final String restOfCommand) {
		this(commandType+" "+restOfCommand);
		completeCommand = commandType + " " + restOfCommand;
	}

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
