package bb.chat.network.packet;

import bb.chat.interfaces.IChatActor;
import bb.chat.interfaces.IMessageHandler;
import bb.chat.interfaces.IPacketDistributor;
import bb.chat.interfaces.IPacketHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 31.08.2014.
 */
public class PacketDistributor implements IPacketDistributor<IPacketHandler> {

	private final IMessageHandler IMH;

	public PacketDistributor(IMessageHandler imh){
		IMH = imh;
	}

    private final List<IPacketHandler> PHList = new ArrayList<IPacketHandler>();

	@Override
    public int registerPacketHandler(IPacketHandler iph){
        if(!PHList.contains(iph)){
            PHList.add(iph);
        }
        return PHList.indexOf(iph);
    }

	@Override
    public void distributePacket(int id,byte[] data,IChatActor sender){

        main:
        for(IPacketHandler iph : PHList){
            for(Class c: iph.getAssociatedPackets()){
                if(c.equals(IMH.getPacketRegistrie().getPacketClassByID(id))){
                    try {
                        IMH.getPacketRegistrie().getNewPacketOfID(id).readFromData(DataIn.newInstance(data.clone()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    continue main;

                }
            }
        }


    }



}
