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
			writeToData(DataOut.newInstance());
			p.readFromData(DataIn.newInstance(dataOut.getBytes()));
			return p;
		} catch(InstantiationException e) {
			e.printStackTrace();
		} catch(IllegalAccessException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected enum PacketState {
		EMPTY, DATA
	}

	protected NetworkState minNetworkState = NetworkState.UNKNOWN;

	/**
	 * if the Packet contains the necessary data to be processed
	 */
	protected PacketState state = PacketState.EMPTY;

	public IPacket() {
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

	/**
	 * You should - use a special constructor to set the data and set the date to DATA or - have getter´s and setter´s
	 * to set the data (than set the State to DATA) or - Override the getStatus function!
	 */

	public PacketState getStatus() {
		return state;
	}

	public abstract void writeToData(DataOut dataOut) throws IOException;


	/**
	 * Should set the state to DATA
	 */
	public abstract void readFromData(DataIn dataIn) throws IOException;

}
