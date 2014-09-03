package bb.chat.network.packet;

import bb.chat.interfaces.IPacket;
import bb.chat.interfaces.IPacketRegistrie;
import bb.chat.network.packet.Handshake.HandshakePacket;
import bb.chat.network.packet.Handshake.PacketSyncPacket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class PacketRegistrie implements IPacketRegistrie<IPacket>{

    //public static final PacketRegistrie INSTANCE = new PacketRegistrie();

    private List<Class<? extends IPacket>> PList = new ArrayList<Class<? extends IPacket>>();

    public PacketRegistrie(){
        registerPacket(HandshakePacket.class);
        registerPacket(PacketSyncPacket.class);
    }

    public int registerPacket(Class<? extends IPacket> p){
        if(!PList.contains(p)){
            PList.add(p);
        }
        return PList.indexOf(p);
    }

    public int getID(Class<? extends IPacket> p){
        return PList.indexOf(p);
    }

    public boolean containsPacket(Class<? extends IPacket> p){
        return PList.contains(p);
    }

    @Override
    @SuppressWarnings("unchecked")
    public PacketSyncPacket getSyncPacket() {
        Class<? extends IPacket>[] pa = new Class[0];
        pa = PList.toArray(pa);
        return new PacketSyncPacket(pa);
    }

    public IPacket getNewPacketOfID(int id) {
        try {
            return PList.get(id).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            System.err.println("Could not Instantiate "+PList.get(id)+" probably missing default Constructor");
            throw new RuntimeException("Instantiation Exception while Instantiating "+PList.get(id));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            System.err.println("Could not Instantiate "+PList.get(id)+" probably missing default Constructor");
            throw new RuntimeException("IllegalAccess Exception while Instantiating "+PList.get(id));
        }
    }

    public Class<? extends IPacket> getPacketClassByID(int id){
        return PList.get(id);
    }

    @Override
    public void HandlePacket(IPacket p) {
        if(p instanceof PacketSyncPacket){
          PList = new ArrayList<Class<? extends IPacket>>();
            Collections.addAll(PList, ((PacketSyncPacket) p).getPackageClasses());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Class<? extends IPacket>[] getAssociatedPackets() {
        return new Class[]{PacketSyncPacket.class};
    }
}
