package bb.chat.network.packet.Handshake;

import bb.chat.interfaces.IPacket;
import bb.chat.network.packet.DataIn;
import bb.chat.network.packet.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class PacketSyncPacket extends IPacket {

	private Class<? extends IPacket>[] classes;

	public PacketSyncPacket(Class<? extends IPacket>[] cs) {
		classes = cs;
	}

	public PacketSyncPacket() {
	}

	public Class<? extends IPacket>[] getPackageClasses() {
		return classes;
	}


	@Override
	public void writeToData(DataOut dataOut) throws IOException {
		dataOut.writeInt(classes.length);
		for(Class c : classes) {
			dataOut.writeUTF(c.getName());
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void readFromData(DataIn dataIn) throws IOException {
		int i = dataIn.readInt();

		classes = new Class[i];
		while(i > 0) {
			try {
				Class c = Class.forName(dataIn.readUTF());
				classes[classes.length - i] = c;
			} catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
			i--;
		}
	}
}
