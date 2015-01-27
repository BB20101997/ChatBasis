package bb.chat.network.handler;

import bb.chat.enums.ServerStatus;
import bb.chat.enums.Side;
import bb.chat.interfaces.*;
import bb.chat.network.packet.Chatting.ChatPacket;
import bb.chat.network.packet.Command.DisconnectPacket;
import bb.chat.network.packet.PacketDistributor;
import bb.chat.network.packet.PacketRegistrie;
import bb.chat.security.BasicPermissionRegistrie;
import bb.chat.security.BasicUserDatabase;
import bb.util.file.database.FileWriter;

import javax.net.ssl.SSLSocket;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class BasicMessageHandler implements IMessageHandler<BasicUserDatabase, BasicPermissionRegistrie> {

	protected final List<IIOHandler> actors = new ArrayList<>();

	private WorkingThread workingRunnable;
	private Thread        workingThread;

	protected String serverMessage = "", serverName = "";
	protected int onlineUserNr = 0, maxOnlineUser = 0;
	protected ServerStatus serverStatus = ServerStatus.UNKNOWN;
	protected final List<String> activeUsers  = new ArrayList<>();

	private static final File configFile = new File("config.fw").getAbsoluteFile();

	protected Side                     side;
	protected BasicPermissionRegistrie permReg;
	protected     IPacketDistributor PD           = new PacketDistributor(this);
	protected     IPacketRegistrie   PR           = new PacketRegistrie();
	private final BasicUserDatabase  userDatabase = new BasicUserDatabase();

	protected SSLSocket socket;

	protected BasicIOHandler IRServer;

	private final List<ICommand> commandList = new ArrayList<>();

	protected IIOHandler localActor;

	private IBasicChatPanel BCP;


	@SuppressWarnings("unchecked")
	public BasicMessageHandler() {
		serverStatus = ServerStatus.STARTING;
		PD.registerPacketHandler(PR);
	}

	public <P extends IPacket> BasicMessageHandler(IPacketRegistrie<? extends P> packetRegistrie, IPacketDistributor<? extends P> packetDistributor) {
		PD = packetDistributor;
		PR = packetRegistrie;
		//noinspection unchecked
		PD.registerPacketHandler(PR);
	}

	@Override
	public void disconnect(IIOHandler a) {

		if(side == Side.CLIENT) {

			if(IRServer != null) {
				sendPackage(new DisconnectPacket(), SERVER);
				IRServer.stop();
				IRServer = null;
			}
			try {
				stopWorkingThread();
				if(socket != null) {
					socket.close();
				}

			} catch(IOException e) {
				e.printStackTrace();
			}


		} else {
			if(a != ALL && a != SERVER) {
				a.stop();
				actors.remove(a);
			} else {
				for(IIOHandler iioHandler : actors) {
					iioHandler.stop();
					actors.remove(iioHandler);
				}
			}
		}

	}

	@Override
	public void save() {
		if(side == Side.SERVER) {
			if(!configFile.exists()) {
				try {

					if(!configFile.createNewFile()) {
						sendPackage(new ChatPacket("Couldn't create save file,changes not saved!", "SYSTEM"),SERVER);
					}

				} catch(IOException e) {
					e.printStackTrace();
				}
			}
			FileWriter fileWriter = new FileWriter();
			fileWriter.add(permReg, "permGen");
			fileWriter.add(userDatabase, "bur");
			try {
				fileWriter.writeToFile(configFile);
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void load() {
		System.out.println("Loading config...");
		if(side == Side.SERVER) {
			if(configFile.exists()) {
				FileWriter fileWriter = new FileWriter();
				try {
					fileWriter.readFromFile(configFile);
					permReg.loadFromFileWriter((FileWriter) fileWriter.get("permGen"));
					userDatabase.loadFromFileWriter((FileWriter) fileWriter.get("bur"));
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
			else{
				System.err.println("Couldn't find config File!");
			}
		}
	}

	public void shutdown() {
		serverStatus = ServerStatus.SHUTDOWN;
		disconnect(ALL);
		stopWorkingThread();
	}

	synchronized void startWorkingThread() {
		if(workingRunnable == null) {
			workingRunnable = new WorkingThread(this);
			workingThread = new Thread(workingRunnable);
			workingThread.start();
		}
	}

	synchronized void stopWorkingThread() {
		if(workingRunnable != null) {
			workingRunnable.stop();
			workingRunnable = null;
			workingThread = null;
		}
	}

	@Override
	public final void setBasicChatPanel(IBasicChatPanel BCP) {
		this.BCP = BCP;
	}

	@Override
	public final void Message(String s) {

		System.out.println("A Message was entered on Side : " + side);
		startWorkingThread();
		workingRunnable.addInput(s);

	}

	@Override
	public final Side getSide() {
		return side;
	}

	@Override
	public final IPacketDistributor getPacketDistributor() {
		return PD;
	}

	@Override
	public final BasicUserDatabase getUserDatabase() {
		return userDatabase;
	}

	@Override
	public final IPacketRegistrie getPacketRegistrie() {
		return PR;
	}

	@Override
	public final BasicPermissionRegistrie getPermissionRegistry() {
		return permReg;
	}

	@Override
	public final void addCommand(java.lang.Class<? extends ICommand> c) {

		try {
			ICommand com = c.newInstance();
			commandList.add(com);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public final ICommand getCommand(String text) {

		for(ICommand c : commandList) {
			if(text.equals(c.getName())) {
				return c;
			}

		}

		return null;
	}

	@Override
	public final IIOHandler getActor() {

		return localActor;
	}

	@Override
	public final IIOHandler getConnectionByName(String s) {

		for(IIOHandler ica : actors) {
			if(ica.getActorName().equals(s)) {
				return ica;
			}
		}

		return null;

	}

	@Override
	public final String[] getHelpForAllCommands() {

		List<String> sList = new ArrayList<>();

		for(ICommand ic : commandList) {
			sList.add(getHelpFromCommand(ic));
		}

		String[] sArr = new String[sList.size()];
		for(int i = 0; i < sList.size(); i++) {
			sArr[i] = sList.get(i);
		}

		return sArr;
	}

	@Override
	public final String getHelpFromCommandName(String s) {

		ICommand c = getCommand(s);
		if(c != null) {
			return getHelpFromCommand(c);
		}
		return null;
	}

	@Override
	public final String getHelpFromCommand(ICommand a) {

		String[] h = a.helpCommand();
		StringBuilder sb = new StringBuilder();
		sb.append(a.getName()).append(":\n");
		for(String s : h) {
			sb.append("\t- ");
			sb.append(s);
			sb.append("\n");
		}

		return sb.toString();

	}

	@Override
	public final void print(String s) {
		System.out.println(s);
		if(BCP!=null) {
			BCP.print(s);
		}
	}

	@Override
	public final void println(String s) {
		System.out.println(s);
		print(s + System.lineSeparator());
	}

	@Override
	public void wipe() {
		System.console().flush();
		BCP.WipeLog();
	}

	@Override
	public final int getMaxUsers() {
		return maxOnlineUser;
	}

	@Override
	public final void setMaxUsers(int i) {
		maxOnlineUser = i;
	}

	@Override
	public final int getOnlineUsers() {
		return onlineUserNr;
	}

	@Override
	public final void setOnlineUsers(int i) {
		onlineUserNr = i;
	}

	@Override
	public final ServerStatus getServerStatus() {
		return serverStatus;
	}

	@Override
	public final void setServerStatus(ServerStatus serverStat) {
		serverStatus = serverStat;
	}

	@Override
	public final String getServerMessage() {
		return serverMessage;
	}

	@Override
	public final void setServerMessage(String msg) {
		serverMessage = msg;
	}

	@Override
	public final String getServerName() {
		return serverName;
	}

	public final void setServerName(String s){
		serverName = s;
	}

	@Override
	public String[] getActiveUserList(){
		return activeUsers.toArray(new String[activeUsers.size()]);
	}

	@Override
	public void setActiveUsers(String[] sArgs) {
		synchronized(activeUsers) {
			activeUsers.clear();
			Collections.addAll(activeUsers, sArgs);
		}
	}

	@Override
	public void addActiveUser(String name) {
		synchronized(activeUsers) {
			if(!activeUsers.contains(name)) {
				activeUsers.add(name);
			}
		}
	}

	@Override
	public void removeActiveUser(String name) {
		synchronized(activeUsers) {
			if(activeUsers.contains(name)){
				activeUsers.remove(name);
			}
		}
	}

	protected class WorkingThread implements Runnable {


		private boolean keepGoing = true;

		public void stop() {
			keepGoing = false;
		}

		private final BasicMessageHandler basicMessageHandler;
		private final LinkedList<String> toProcess = new LinkedList<>();

		public WorkingThread(BasicMessageHandler bmh) {
			basicMessageHandler = bmh;
		}

		public void addInput(String s) {
			toProcess.add(s);
		}

		public void run() {
			String s;
			do {
				if(!toProcess.isEmpty()) {

					s = toProcess.pollFirst();

					if(s.startsWith("/")) {

						String[] strA = s.split(" ");
						strA[0] = strA[0].replace("/", "");
						ICommand c = getCommand(strA[0]);

						if(c != null) {
							if(side == Side.SERVER) {
								c.runCommand(s, basicMessageHandler);
							} else if(side == Side.CLIENT) {
								c.runCommand(s, basicMessageHandler);
							}
						} else {
							println("[" + localActor.getActorName() + "]Please enter a valid command!");
						}

					} else {

						sendPackage(new ChatPacket(s, getActor().getActorName()),ALL);

						if(side == Side.SERVER) {
							println("[" + localActor.getActorName() + "] " + s);
						}

					}
				}
			} while(keepGoing);
		}
	}
}