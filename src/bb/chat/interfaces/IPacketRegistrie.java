package bb.chat.interfaces;

import bb.chat.network.packet.Handshake.PacketSyncPacket;

/**
 * Created by BB20101997 on 03.09.2014.
 */
public interface IPacketRegistrie<P extends IPacket> extends IPacketHandler<P> {
	int registerPacket(Class<? extends P> p);

	int getID(Class<? extends P> p);

	boolean containsPacket(Class<? extends IPacket> p);

	PacketSyncPacket getSyncPacket();

	P getNewPacketOfID(int id);

	Class<? extends P> getPacketClassByID(int id);
}
