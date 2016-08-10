package bb.chat.network.packet.handshake;

import bb.net.interfaces.APacket;
import bb.net.packets.DataIn;
import bb.net.packets.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 23.11.2014.
 */
public class LoginPacket extends APacket {

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
