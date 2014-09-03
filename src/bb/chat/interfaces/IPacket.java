package bb.chat.interfaces;

import bb.chat.network.NetworkState;
import bb.chat.network.packet.DataIn;
import bb.chat.network.packet.DataOut;

import java.io.IOException;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public abstract class IPacket {

    protected NetworkState minNetworkState = NetworkState.UNKNOWN;

    public IPacket(){
        try {
            getClass().getConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            throw new RuntimeException("Missing default constructor in Packet class : "+getClass().getName());
        }

    }

    public final NetworkState allowedFrom(){
        return minNetworkState;
    }

    public abstract void writeToData(DataOut dataOut) throws IOException;

    public abstract void readFromData(DataIn dataIn) throws IOException;

}
