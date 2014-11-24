package bb.chat.network.handler;

import bb.chat.enums.Side;
import bb.chat.interfaces.IIOHandler;
import bb.chat.interfaces.IMessageHandler;
import bb.chat.interfaces.IPacket;
import bb.chat.interfaces.IPacketRegistrie;
import bb.chat.network.packet.Chatting.ChatPacket;
import bb.chat.network.packet.Command.*;
import bb.chat.network.packet.Handshake.LoginPacket;
import bb.chat.network.packet.Handshake.SignUpPacket;

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
		addAssociatedPacket(DisconnectPacket.class);
		addAssociatedPacket(RenamePacket.class);
		addAssociatedPacket(StopPacket.class);
		addAssociatedPacket(WhisperPacket.class);
		addAssociatedPacket(LoginPacket.class);
		addAssociatedPacket(SignUpPacket.class);
		addAssociatedPacket(SavePacket.class);
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
		} catch(NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		}

	}


	@SuppressWarnings("UnusedParameters")
	private void handlePacket(ChatPacket cp, IIOHandler sender) {
		if(IMH.getSide() == Side.CLIENT) {
			IMH.println("[" + cp.Sender + "] " + cp.message);
		} else {
			IMH.println("[" + cp.Sender + "] " + cp.message);
			IMH.setEmpfaenger(IMessageHandler.ALL);
			IMH.sendPackage(cp);
		}
	}

	@SuppressWarnings("UnusedParameters")
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

	@SuppressWarnings("UnusedParameters")
	private void handlePacket(DisconnectPacket dp, IIOHandler sender) {
		IMH.disconnect(sender);
		IMH.setEmpfaenger(IMessageHandler.ALL);
		IMH.sendPackage(new ChatPacket(sender.getActorName() + " disconnected!", IMH.getActor().getActorName()));
		IMH.println("[" + IMH.getActor().getActorName() + "] " + sender.getActorName() + " disconnected!");
	}

	@SuppressWarnings("UnusedParameters")
	private void handlePacket(StopPacket sp, IIOHandler sender) {
		if(IMH.getSide() == Side.SERVER) {
			IMH.disconnect(IMessageHandler.ALL);
			IMH.shutdown();
		}
	}

	@SuppressWarnings("UnusedParameters")
	private void handlePacket(WhisperPacket wp, IIOHandler sender) {
		if(IMH.getSide() == Side.SERVER && !wp.getReceiver().equals(IMH.getActor().getActorName())) {
			IMH.setEmpfaenger(IMH.getUserByName(wp.getReceiver()));
			IMH.sendPackage(wp);
		} else {
			if(IMH.getActor().getActorName().equals(wp.getReceiver())) {
				IMH.println("[" + wp.getSender() + " whispered to you: ] \"" + wp.getMessage() + "\"");
			}
		}
	}

	@SuppressWarnings("UnusedParameters")
	private void handlePacket(SavePacket sp,IIOHandler sender){
		if(IMH.getSide() == Side.SERVER){
			IMH.save();
		}
	}

}
