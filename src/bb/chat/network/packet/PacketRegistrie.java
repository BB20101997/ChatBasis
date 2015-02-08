package bb.chat.network.packet;

import bb.chat.interfaces.IIOHandler;
import bb.chat.interfaces.APacket;
import bb.chat.interfaces.IPacketRegistrie;
import bb.chat.network.packet.Handshake.HandshakePacket;
import bb.chat.network.packet.Handshake.PacketSyncPacket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class PacketRegistrie implements IPacketRegistrie<APacket> {

	private List<Class<? extends APacket>> PList = new ArrayList<>();

	public PacketRegistrie() {
		registerPacket(HandshakePacket.class);
		registerPacket(PacketSyncPacket.class);
	}

	public int registerPacket(Class<? extends APacket> p) {
		if(!PList.contains(p)) {
			PList.add(p);
		}
		return PList.indexOf(p);
	}

	public int getID(Class<? extends APacket> p) {
		return PList.indexOf(p);
	}

	public boolean containsPacket(Class<? extends APacket> p) {
		return PList.contains(p);
	}

	@Override
	@SuppressWarnings("unchecked")
	public PacketSyncPacket getSyncPacket() {
		Class<? extends APacket>[] pa = new Class[0];
		pa = PList.toArray(pa);
		return new PacketSyncPacket(pa);
	}

	public APacket getNewPacketOfID(int id) {
		try {
			return PList.get(id).newInstance();
		} catch(InstantiationException e) {
			e.printStackTrace();
			System.err.println("Could not Instantiate " + PList.get(id) + " probably missing default Constructor");
			throw new RuntimeException("Instantiation Exception while Instantiating " + PList.get(id));
		} catch(IllegalAccessException e) {
			e.printStackTrace();
			System.err.println("Could not Instantiate " + PList.get(id) + " probably missing default Constructor");
			throw new RuntimeException("IllegalAccess Exception while Instantiating " + PList.get(id));
		}
	}

	public Class<? extends APacket> getPacketClassByID(int id) {
		return PList.get(id);
	}

	@Override
	public void HandlePacket(APacket p, IIOHandler sender) {
		if(p instanceof PacketSyncPacket) {
			PList = new ArrayList<>();
			Collections.addAll(PList, ((PacketSyncPacket) p).getPackageClasses());
		} else if(p instanceof HandshakePacket) {
			sender.receivedHandshake();
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class<? extends APacket>[] getAssociatedPackets() {
		return new Class[]{PacketSyncPacket.class};
	}
}
