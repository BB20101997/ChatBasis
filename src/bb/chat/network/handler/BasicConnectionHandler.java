package bb.chat.network.handler;

import bb.chat.enums.ServerStatus;
import bb.chat.enums.Side;
import bb.chat.interfaces.*;
import bb.chat.network.packet.Chatting.ChatPacket;
import bb.chat.network.packet.Command.DisconnectPacket;
import bb.chat.security.BasicPermissionRegistrie;
import bb.chat.security.BasicUserDatabase;

import javax.net.ssl.SSLSocket;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class BasicConnectionHandler implements IConnectionHandler<BasicUserDatabase, BasicPermissionRegistrie> {

	protected final List<IIOHandler> actors = new ArrayList<>();

	private WorkingThread workingRunnable;
	private Thread        workingThread;

	protected String serverMessage = "", serverName = "";
	protected int onlineUserNr = 0, maxOnlineUser = 0;
	protected       ServerStatus serverStatus = ServerStatus.NOT_STARTED;
	protected final List<String> activeUsers  = new ArrayList<>();

	private static final File configFile = new File("config.fw").getAbsoluteFile();

	protected Side                                               side;
	private   IChat<BasicUserDatabase, BasicPermissionRegistrie> iChat;

	protected SSLSocket socket;

	protected BasicIOHandler IRServer;

	private final List<ICommand> commandList = new ArrayList<>();

	protected IIOHandler localActor;

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

	public void shutdown() {
		serverStatus = ServerStatus.SHUTDOWN;
		disconnect(ALL);
		stopWorkingThread();
	}

	final synchronized void startWorkingThread() {
		if(workingRunnable == null) {
			workingRunnable = new WorkingThread(this);
			workingThread = new Thread(workingRunnable);
			workingThread.start();
		}
	}

	final synchronized void stopWorkingThread() {
		if(workingRunnable != null) {
			workingRunnable.stop();
			workingRunnable = null;
			workingThread = null;
		}
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
	public final IChat<BasicUserDatabase, BasicPermissionRegistrie> getIChatInstance() {
		if(iChat==null)System.out.println("IChat is null!At get IChatInstance()");
		return iChat;
	}

	@Override
	public final void setIChatInstance(IChat<BasicUserDatabase, BasicPermissionRegistrie> ic) {
		if(ic==null)System.out.println("ic is null!At setIChatInstance(ic)");
		iChat = ic;
	}

	@Override
	public final void print(String s) {
		System.out.println(s);
		if(iChat.getBasicChatPanel() != null) {
			iChat.getBasicChatPanel().print(s);
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
		//TODO check how to clear the console
		if(iChat.getBasicChatPanel() != null) {
			iChat.getBasicChatPanel().WipeLog();
		}
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

	public final void setServerName(String s) {
		serverName = s;
	}

	@Override
	public final String[] getActiveUserList() {
		return activeUsers.toArray(new String[activeUsers.size()]);
	}

	@Override
	public final void setActiveUsers(String[] sArgs) {
		synchronized(activeUsers) {
			activeUsers.clear();
			Collections.addAll(activeUsers, sArgs);
		}
	}

	@Override
	public final void addActiveUser(String name) {
		synchronized(activeUsers) {
			if(!activeUsers.contains(name)) {
				activeUsers.add(name);
			}
		}
	}

	@Override
	public final void removeActiveUser(String name) {
		synchronized(activeUsers) {
			if(activeUsers.contains(name)) {
				activeUsers.remove(name);
			}
		}
	}

	protected class WorkingThread implements Runnable {


		private boolean keepGoing = true;

		public void stop() {
			keepGoing = false;
		}

		private final BasicConnectionHandler basicMessageHandler;
		private final LinkedList<String> toProcess = new LinkedList<>();

		public WorkingThread(BasicConnectionHandler bmh) {
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
						ICommand c = getIChatInstance().getCommandRegestry().getCommand(strA[0]);

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

						sendPackage(new ChatPacket(s, getActor().getActorName()), ALL);

						if(side == Side.SERVER) {
							println("[" + localActor.getActorName() + "] " + s);
						}

					}
				}
			} while(keepGoing);
		}
	}
}