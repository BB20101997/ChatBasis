package bb.chat.network.handler;

import bb.chat.interfaces.IMessageHandler;
import bb.chat.interfaces.IPacket;
import bb.chat.interfaces.IPacketHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 31.08.2014.
 */
public abstract class BasicPacketHandler<P extends IPacket> implements IPacketHandler<P> {

	final IMessageHandler IMH;

	private final List<Class<? extends P>> CList = new ArrayList<>();

	BasicPacketHandler(IMessageHandler iMessageHandler) {
		IMH = iMessageHandler;
	}

	@SuppressWarnings("unchecked")
	void addAssociatedPacket(Class<? extends P> cp) {
		if(!CList.contains(cp)) {
			CList.add(cp);
			if(!IMH.getPacketRegistrie().containsPacket(cp)) {
				IMH.getPacketRegistrie().registerPacket(cp);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Class<P>[] getAssociatedPackets() {
		return CList.toArray(new Class[CList.size()]);
	}

}
