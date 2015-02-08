package bb.chat.network.handler;

import bb.chat.interfaces.APacket;
import bb.chat.interfaces.IConnectionHandler;
import bb.chat.interfaces.IIOHandler;
import bb.chat.interfaces.IPacketHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 31.08.2014.
 */
public abstract class BasicPacketHandler<P extends APacket> implements IPacketHandler<P> {

	final IConnectionHandler IMH;

	private final List<Class<? extends P>> CList = new ArrayList<>();

	BasicPacketHandler(IConnectionHandler iConnectionHandler) {
		IMH = iConnectionHandler;
	}

	@SuppressWarnings("unchecked")
	public final void HandlePacket(APacket aPacket, IIOHandler sender) {

		int id = IMH.getIChatInstance().getPacketRegistrie().getID(aPacket.getClass());

		System.out.println("DefaultPacketHandler : " + aPacket.getClass() + ", ID : " + id);

		try {
			Method m = getClass().getDeclaredMethod("handlePacket", aPacket.getClass(), IIOHandler.class);
			m.invoke(this, aPacket, sender);
		} catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	void addAssociatedPacket(Class<? extends P> cp) {
		if(!CList.contains(cp)) {
			CList.add(cp);
			if(!IMH.getIChatInstance().getPacketRegistrie().containsPacket(cp)) {
				IMH.getIChatInstance().getPacketRegistrie().registerPacket(cp);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public Class<P>[] getAssociatedPackets() {
		return CList.toArray(new Class[CList.size()]);
	}

}
