package bb.chat.network.packet.command;

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
public class RenamePacket extends APacket {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(RenamePacket.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}
	
	public String newName;
	public String oldName;

	@SuppressWarnings("unused")
	public RenamePacket() {
		this("","");
	}

	public RenamePacket(String old, String newN) {
		log.fine("RenamePacket from "+old+" to "+newN+"!");
		oldName = old;
		newName = newN;
	}

	@Override
	public void writeToData(DataOut dataOut) throws IOException {
		dataOut.writeUTF(newName);
		dataOut.writeUTF(oldName);
	}

	@Override
	public void readFromData(DataIn dataIn) throws IOException {
		newName = dataIn.readUTF();
		oldName = dataIn.readUTF();
	}

	@Override
	public String toString() {
		return MessageFormat.format("[RenamePacket] Renaming from {0} to {1}", oldName, newName);
	}
}
