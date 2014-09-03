package bb.chat.network.packet;

import bb.chat.interfaces.IPacket;
import bb.chat.interfaces.IPacketHandler;
import bb.chat.network.Side;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 31.08.2014.
 */
public abstract class BasicPacketHandler implements IPacketHandler {

    public final Side side;

    private final List<Class<? extends IPacket>> CList = new ArrayList<Class<? extends IPacket>>();

    public BasicPacketHandler(Side side){
        this.side = side;
    }

    protected void addAssociatedPacket(Class<? extends IPacket> cp){
        if(!CList.contains(cp)){
            CList.add(cp);
        }
    }

    @SuppressWarnings("unchecked")
    public Class<? extends IPacket>[] getAssociatedPackets(){
          return CList.toArray(new Class[CList.size()]);
    }

    public abstract void HandlePacket(IPacket p);

}
