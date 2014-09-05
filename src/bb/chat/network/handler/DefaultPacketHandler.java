package bb.chat.network.handler;

import bb.chat.interfaces.IIOHandler;
import bb.chat.interfaces.IMessageHandler;
import bb.chat.interfaces.IPacket;
import bb.chat.interfaces.IPacketRegistrie;
import bb.chat.network.Side;
import bb.chat.network.packet.Chatting.ChatPacket;
import bb.chat.network.packet.Command.DisconnectPacket;
import bb.chat.network.packet.Command.StopPacket;
import bb.chat.network.packet.Command.WhisperPacket;

/**
 * Created by BB20101997 on 05.09.2014.
 */
public final class DefaultPacketHandler extends BasicPacketHandler<IPacket> {

	public DefaultPacketHandler(IMessageHandler imh) {
		super(imh);
		addAssociatedPacket(ChatPacket.class);
		imh.getPacketRegistrie().registerPacket(ChatPacket.class);
		addAssociatedPacket(DisconnectPacket.class);
		imh.getPacketRegistrie().registerPacket(DisconnectPacket.class);
		addAssociatedPacket(StopPacket.class);
		imh.getPacketRegistrie().registerPacket(StopPacket.class);
		addAssociatedPacket(WhisperPacket.class);
		imh.getPacketRegistrie().registerPacket(WhisperPacket.class);
	}

	@Override
	public void HandlePacket(IPacket iPacket, IIOHandler sender) {

		IPacketRegistrie PR = IMH.getPacketRegistrie();
		int id = PR.getID(iPacket.getClass());

		System.out.println("DefaultPacketHandler : " + iPacket.getClass());

		if(id == PR.getID(ChatPacket.class)) {

			System.out.println("Handeling a ChatPacket");
			if(iPacket instanceof ChatPacket) {
				ChatPacket cp = (ChatPacket) iPacket;
				if(IMH.getSide() == Side.CLIENT) {
					IMH.println("[" + cp.Sender + "] " + ((ChatPacket) iPacket).message);
				} else {
					IMH.println("[" + cp.Sender + "] " + ((ChatPacket) iPacket).message);
					IMH.setEmpfaenger(IMessageHandler.ALL);
					IMH.sendPackage(iPacket);
				}
			}
			return;
		}


	}
}
