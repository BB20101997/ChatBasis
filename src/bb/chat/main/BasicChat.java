package bb.chat.main;

import bb.chat.interfaces.*;
import bb.chat.network.packet.Chatting.ChatPacket;
import bb.chat.network.packet.PacketDistributor;
import bb.chat.security.BasicPermissionRegistrie;
import bb.chat.security.BasicUserDatabase;
import bb.util.file.database.FileWriter;

import java.io.File;
import java.io.IOException;

/**
 * Created by BB20101997 on 30.01.2015.
 */
public class BasicChat implements IChat<BasicUserDatabase, BasicPermissionRegistrie> {

	private static final File configFile = new File("config.fw").getAbsoluteFile();

	private IConnectionHandler<BasicUserDatabase, BasicPermissionRegistrie> imh;
	private IPacketRegistrie                                                packetRegistrie;
	private BasicPermissionRegistrie                                        basicPermissionRegistrie;
	private PacketDistributor                                               packetDistributor;
	private BasicUserDatabase                                               basicUserDatabase;
	private ICommandRegistry                                                commandRegistry;
	private IBasicChatPanel basicChatPanel = null;

	public BasicChat(IConnectionHandler<BasicUserDatabase, BasicPermissionRegistrie> imessagehandler, IPacketRegistrie pr, BasicPermissionRegistrie bpr, PacketDistributor pd, BasicUserDatabase bud,ICommandRegistry icr) {
		this.imh = imessagehandler;
		packetRegistrie = pr;
		basicPermissionRegistrie = bpr;
		packetDistributor = pd;
		basicUserDatabase = bud;
		commandRegistry = icr;
		imh.setIChatInstance(this);
		imh.initiate();
		load();
	}

	@Override
	public IConnectionHandler<BasicUserDatabase, BasicPermissionRegistrie> getIMessageHandler() {
		return imh;
	}

	@Override
	public IPacketRegistrie getPacketRegistrie() {
		return packetRegistrie;
	}

	@Override
	public BasicPermissionRegistrie getPermissionRegistry() {
		return basicPermissionRegistrie;
	}

	@Override
	public IPacketDistributor getPacketDistributor() {
		return packetDistributor;
	}

	@Override
	public BasicUserDatabase getUserDatabase() {
		return basicUserDatabase;
	}

	@Override
	public IBasicChatPanel getBasicChatPanel() {
		return basicChatPanel;
	}

	@Override
	public ICommandRegistry getCommandRegestry() {
		return commandRegistry;
	}

	@Override
	public void setBasicChatPanel(IBasicChatPanel bcp) {
		basicChatPanel = bcp;
	}

	@Override
	public void save() {
		if(!configFile.exists()) {
			try {

				if(!configFile.createNewFile()) {
					//TODO
					imh.sendPackage(new ChatPacket("Couldn't create save file,changes not saved!", "SYSTEM"), IConnectionHandler.SERVER);
				}

			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		FileWriter fileWriter = new FileWriter();
		fileWriter.add(basicPermissionRegistrie, "permReg");
		fileWriter.add(basicUserDatabase, "bur");
		try {
			fileWriter.writeToFile(configFile);
		} catch(IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void load() {
		System.out.println("Loading config...");

		if(configFile.exists()) {
			FileWriter fileWriter = new FileWriter();
			try {
				fileWriter.readFromFile(configFile);
				basicPermissionRegistrie.loadFromFileWriter((FileWriter) fileWriter.get("permReg"));
				basicUserDatabase.loadFromFileWriter((FileWriter) fileWriter.get("bur"));
			} catch(IOException e) {
				e.printStackTrace();
			}
		} else {
			System.err.println("Couldn't find config File!");
		}

	}

	@Override
	public void shutdown() {
		imh.disconnect(IConnectionHandler.ALL);

	}
}
