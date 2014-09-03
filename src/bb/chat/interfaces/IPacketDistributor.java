package bb.chat.interfaces;

/**
 * Created by BB20101997 on 03.09.2014.
 */
public interface IPacketDistributor<PH extends IPacketHandler> {


	/**
	 * if the instance of PH is already registered it should not be registered twice
	 * and the id of the old registration should be returned
	 * */
	int registerPacketHandler(PH ph);

	/**
	 * @param id The id of the Packet the data is from
	 * @param data the data of the packet to be created and distributed
	 * @param sender the IChatActor that received/send the Packet
	 *               (should be the IOHandler that received the packet)
	 */
	void distributePacket(int id,byte[] data,IChatActor sender);

}
