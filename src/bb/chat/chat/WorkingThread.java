package bb.chat.chat;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.chatting.ChatPacket;
import bb.net.enums.Side;
import bb.net.interfaces.IIOHandler;

import java.util.LinkedList;

/**
 * Created by BB20101997 on 26.03.2015.
 * Handles outbound Messages
 */
public class WorkingThread {

	final   IChat           iChat;
	private WorkingRunnable workingRunnable;
	private Thread          workingThread;

	public WorkingThread(IChat ic) {
		iChat = ic;
	}

	//starts the working thread if not running els restarts
	final synchronized void start() {
		if(workingRunnable == null) {
			workingRunnable = new WorkingRunnable();
		}
		if(workingThread != null) {
			workingRunnable.stop();
			//noinspection StatementWithEmptyBody
			while(workingThread.isAlive()){}
		}
		workingThread = new Thread(workingRunnable);
		workingThread.setName("WorkingThread");
		workingThread.setDaemon(true);
		workingThread.start();
	}

	//if the working thread is set stops it
	final synchronized void stop() {
		if(workingRunnable != null) {
			workingRunnable.stop();
			workingRunnable = null;
			workingThread = null;
		}
	}


	//adds the given string to the list of Strings to be processed
	//should be thread safe
	//pass down
	void addLine(String s) {
		workingRunnable.addInput(s);
	}

	@SuppressWarnings("HardcodedFileSeparator")
	private class WorkingRunnable implements Runnable {


		private boolean keepGoing = true;

		//makes the thread stop next iteration
		public void stop() {
			keepGoing = false;
		}

		//the list of strings to process
		private final LinkedList<String> toProcess = new LinkedList<>();

		//adds the given string to the list of Strings to be processed
		public synchronized void addInput(String s) {
			toProcess.add(s);
		}

		public void run() {
			Side side = iChat.getIConnectionManager().getSide();
			String s;
			do {
				if(!toProcess.isEmpty()) {

					s = toProcess.pollFirst();

					if(s.startsWith(ICommand.COMMAND_INIT_STRING)) {

						String[] strA = s.split(" ");
						strA[0] = strA[0].replace(ICommand.COMMAND_INIT_STRING, "");
						ICommand ic = iChat.getCommandRegistry().getCommand(strA[0]);

						if(ic != null) {
							if(side == Side.SERVER) {
								ic.runCommand(s, iChat);
							} else {
								ic.runCommand(s, iChat);
							}
						} else {
							iChat.getBasicChatPanel().println("[" + iChat.getLOCAL().getActorName() + "]Please enter a valid command!");
						}

					} else {

						String aName = iChat.getLOCAL().getActorName();
						IIOHandler iA = iChat.getIConnectionManager().ALL();

						iChat.getIConnectionManager().sendPackage(new ChatPacket(s, aName), iA);

						if(side == Side.SERVER) {
							iChat.getBasicChatPanel().println("[" + iChat.getLOCAL().getActorName() + "] " + s);
						}

					}
				}
			} while(keepGoing);
		}
	}

}
