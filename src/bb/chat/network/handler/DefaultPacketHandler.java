package bb.chat.network.handler;

import bb.chat.enums.Side;
import bb.chat.interfaces.IIOHandler;
import bb.chat.interfaces.IMessageHandler;
import bb.chat.interfaces.IPacket;
import bb.chat.interfaces.IPacketRegistrie;
import bb.chat.network.packet.Chatting.ChatPacket;
import bb.chat.network.packet.Command.DisconnectPacket;
import bb.chat.network.packet.Command.RenamePacket;
import bb.chat.network.packet.Command.StopPacket;
import bb.chat.network.packet.Command.WhisperPacket;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by BB20101997 on 05.09.2014.
 */
public final class DefaultPacketHandler extends BasicPacketHandler<IPacket> {

	@SuppressWarnings("unchecked")
	public DefaultPacketHandler(IMessageHandler imh) {
		super(imh);
		addAssociatedPacket(ChatPacket.class);
		imh.getPacketRegistrie().registerPacket(ChatPacket.class);
		addAssociatedPacket(DisconnectPacket.class);
		imh.getPacketRegistrie().registerPacket(DisconnectPacket.class);
		addAssociatedPacket(RenamePacket.class);
		imh.getPacketRegistrie().registerPacket(RenamePacket.class);
		addAssociatedPacket(StopPacket.class);
		imh.getPacketRegistrie().registerPacket(StopPacket.class);
		addAssociatedPacket(WhisperPacket.class);
		imh.getPacketRegistrie().registerPacket(WhisperPacket.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	public void HandlePacket(IPacket iPacket, IIOHandler sender) {

		IPacketRegistrie PR = IMH.getPacketRegistrie();
		int id = PR.getID(iPacket.getClass());

		System.out.println("DefaultPacketHandler : " + iPacket.getClass() + ", ID : " + id);

		try {
			Method m = getClass().getDeclaredMethod("handlePacket", iPacket.getClass(), IIOHandler.class);
			m.invoke(this, iPacket, sender);
		} catch(NoSuchMethodException e) {
			e.printStackTrace();
		} catch(InvocationTargetException e) {
			e.printStackTrace();
		} catch(IllegalAccessException e) {
			e.printStackTrace();
		}

	}


	private void handlePacket(ChatPacket cp, IIOHandler sender) {
		if(IMH.getSide() == Side.CLIENT) {
			IMH.println("[" + cp.Sender + "] " + cp.message);
		} else {
			IMH.println("[" + cp.Sender + "] " + cp.message);
			IMH.setEmpfaenger(IMessageHandler.ALL);
			IMH.sendPackage(cp);
		}
	}

	private void handlePacket(RenamePacket rp, IIOHandler sender) {
		if(IMH.getSide() == Side.CLIENT) {
			if(IMH.getActor().getActorName().equals(rp.oldName)) {
				IMH.getActor().setActorName(rp.newName);
			}
		} else {
			IMH.getUserByName(rp.oldName).setActorName(rp.newName);
			IMH.setEmpfaenger(IMessageHandler.ALL);
			IMH.sendPackage(new ChatPacket(rp.oldName + " is now known as " + rp.newName, IMH.getActor().getActorName()));
		}
	}

	private void handlePacket(DisconnectPacket dp, IIOHandler sender) {
		IMH.disconnect(sender);
		IMH.setEmpfaenger(IMessageHandler.ALL);
		IMH.sendPackage(new ChatPacket(sender.getActorName() + " disconnected!", IMH.getActor().getActorName()));
		IMH.println("[" + IMH.getActor().getActorName() + "] " + sender.getActorName() + " disconnected!");
	}

	private void handlePacket(StopPacket sp, IIOHandler sender) {
		if(IMH.getSide() == Side.SERVER) {
			IMH.disconnect(IMessageHandler.ALL);
			IMH.shutdown();
		}
	}

}
