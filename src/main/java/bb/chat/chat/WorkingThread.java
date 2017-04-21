package bb.chat.chat;

import bb.chat.basis.BasisConstants;
import bb.chat.enums.Bundles;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.chatting.ChatPacket;
import bb.net.enums.Side;
import bb.net.interfaces.IIOHandler;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

/**
 * Created by BB20101997 on 26.03.2015.
 * Handles outbound Messages
 */
public class WorkingThread {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger logger      = BasisConstants.getLogger(WorkingThread.class);
	public static final String  THREAD_NAME = "WorkingThread";

	final   IChat           iChat;
	private WorkingRunnable workingRunnable;
	private Thread          workingThread;

	public WorkingThread(IChat ic) {
		iChat = ic;
	}

	//starts the working thread if not running els restarts
	final synchronized void start() {
		logger.fine(Bundles.LOG_TEXT.getString("log.wt.start"));
		if(workingRunnable == null) {
			workingRunnable = new WorkingRunnable();
		}
		if(workingThread != null) {
			workingRunnable.stop();
			//noinspection StatementWithEmptyBody
			while(workingThread.isAlive()){}
		}
		workingThread = new Thread(workingRunnable);
		workingThread.setName(THREAD_NAME);
		workingThread.setDaemon(true);
		workingThread.start();
	}

	//if the working thread is set stops it
	synchronized void stop() {
		logger.fine(Bundles.LOG_TEXT.getString("log.wt.stop"));
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
		logger.fine(Bundles.LOG_TEXT.getString("log.wt.add"));
		workingRunnable.addInput(s);
	}

	private class WorkingRunnable implements Runnable {

		//the list of strings to process
    private final Queue<String> toProcess = new ConcurrentLinkedQueue<>();

		private boolean keepGoing = true;
		private Side side;

		public WorkingRunnable(){
			side = iChat.getIConnectionManager().getSide();
		}

		//makes the thread stop next iteration
		public synchronized void stop() {
			keepGoing = false;
			notify();
		}

		//adds the given string to the list of Strings to be processed
		public synchronized void addInput(String s) {
			toProcess.add(s);
			notify();
		}

		public void run() {
			String s;
			String init = Bundles.COMMAND.getString("init_string");
			do {
				if(!toProcess.isEmpty()) {

					synchronized(this) {
						s = toProcess.poll();
					}

					if(s.startsWith(init)) {

						String[] strA = s.split(" ");
						strA[0] = strA[0].replace(init, "");
						ICommand ic = iChat.getCommandRegistry().getCommand(strA[0]);

						if(ic != null) {
							if(side == Side.SERVER) {
								ic.runCommand(s, iChat);
							} else {
								ic.runCommand(s, iChat);
							}
						} else {
							iChat.getBasicChatPanel().println(Bundles.COMMAND.getString("wt.invalid"));
						}

					} else {

						String aName = iChat.getLOCALActor().getActorName();
						IIOHandler iA = iChat.getIConnectionManager().ALL();

						iChat.getIConnectionManager().sendPackage(new ChatPacket(s, aName), iA);

						if(side == Side.SERVER) {
							iChat.getBasicChatPanel().println("[" + iChat.getLOCALActor().getActorName() + "] " + s);
						}

					}
				}else{
					while(toProcess.isEmpty()&&keepGoing){
						synchronized(this){
							try {
								wait();
							}
							catch(InterruptedException ignored) {
							}
						}
					}
				}
			} while(keepGoing);
		}
	}

}
