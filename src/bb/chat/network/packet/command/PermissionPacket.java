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
	public  String cmd;
	public  String restCmd;
	private String completeCommand;

	public PermissionPacket() {}

	public PermissionPacket(String commandType, String restOfCommand) {
		cmd = commandType;
		restCmd = restOfCommand;
		completeCommand = cmd + " " + restCmd;
	}

	public PermissionPacket(String command){
		completeCommand = command;
		String[] sA = completeCommand.split(" ", 1);
		if(sA.length > 0) {
			cmd = sA[0];
			if(sA.length > 1) {
				restCmd = sA[1];
			}
		}
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
		String[] sA = completeCommand.split(" ", 1);
		if(sA.length > 0) {
			cmd = sA[0];
			if(sA.length >= 2) {
				restCmd = sA[1];
			}
		}
	}
}
