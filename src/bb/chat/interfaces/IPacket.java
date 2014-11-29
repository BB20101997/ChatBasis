package bb.chat.interfaces;

import bb.chat.enums.NetworkState;
import bb.chat.network.packet.DataIn;
import bb.chat.network.packet.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public abstract class IPacket {


	public IPacket copy() {
		try {
			IPacket p = this.getClass().newInstance();
			DataOut dataOut = DataOut.newInstance();
			writeToData(dataOut);
			p.readFromData(DataIn.newInstance(dataOut.getBytes()));
			return p;
		} catch(InstantiationException | IllegalAccessException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("CanBeFinal")
	private NetworkState minNetworkState = NetworkState.UNKNOWN;

	protected IPacket() {
		try {
			getClass().getConstructor(new Class[0]);
		} catch(NoSuchMethodException e) {
			e.printStackTrace();
			throw new RuntimeException("Missing default constructor in Packet class : " + getClass().getName());
		}
	}


	public final NetworkState allowedFrom() {
		return minNetworkState;
	}

	public abstract void writeToData(DataOut dataOut) throws IOException;

	public abstract void readFromData(DataIn dataIn) throws IOException;

}
