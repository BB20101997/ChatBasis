package bb.chat.network.packet.handshake;

import bb.net.interfaces.APacket;
import bb.net.packets.DataIn;
import bb.net.packets.DataOut;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.io.IOException;
import java.util.logging.Logger;

import static bb.chat.basis.BasisConstants.LOG_NAME;

/**
 * Created by BB20101997 on 19.02.2015.
 */
@SuppressWarnings("ClassIndependentOfModule")
public class GuestPacket extends APacket {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(GuestPacket.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}

	public int number = 0;

	@Override
	public void writeToData(DataOut dataOut) throws IOException {
		dataOut.writeInt(number);
	}

	@Override
	public void readFromData(DataIn dataIn) throws IOException {
		number = dataIn.readInt();
	}
}
