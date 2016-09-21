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
 * Created by BB20101997 on 23.11.2014.
 */
public class LoginPacket extends APacket {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(LoginPacket.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}

	private String userName;
	private String userPasswd;

	public void setUsername(String name) {
		userName = name;
	}

	public void setPassword(String passwd) {
		userPasswd = passwd;
	}

	public String getUsername() {
		return userName;
	}

	public String getPassword() {
		return userPasswd;
	}

	@Override
	public void writeToData(DataOut dataOut) throws IOException {
		dataOut.writeUTF(userName);
		dataOut.writeUTF(userPasswd);
	}

	@Override
	public void readFromData(DataIn dataIn) throws IOException {
		userName = dataIn.readUTF();
		userPasswd = dataIn.readUTF();
	}
}
