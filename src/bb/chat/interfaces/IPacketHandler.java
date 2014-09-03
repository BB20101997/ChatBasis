package bb.chat.interfaces;

/**
 * Created by BB20101997 on 31.08.2014.
 */
public interface IPacketHandler<P extends IPacket> {

    void HandlePacket(P p);
    Class<? extends P>[] getAssociatedPackets();

}
