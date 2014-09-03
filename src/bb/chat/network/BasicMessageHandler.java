package bb.chat.network;

import bb.chat.interfaces.*;
import bb.chat.network.packet.Chatting.ChatPacket;
import bb.chat.network.packet.PacketDistributor;
import bb.chat.network.packet.PacketRegistrie;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public abstract class BasicMessageHandler implements IMessageHandler {

    final List<IChatActor> actors = new ArrayList<IChatActor>();

    private WorkingThread workingRunnable;
    private Thread workingThread;

    protected IChatActor Target;

    protected Side side;
	protected IPacketDistributor PD = new PacketDistributor(this);
	protected IPacketRegistrie PR = new PacketRegistrie();

    protected SSLSocket socket;

    protected IOHandler IRServer = null;

    private final List<ICommand> commandList = new ArrayList<ICommand>();

    protected IChatActor localActor;

    private final List<IBasicChatPanel> BCPList = new ArrayList<IBasicChatPanel>();


	@SuppressWarnings("unchecked")
	public BasicMessageHandler(){
		PD = new PacketDistributor(this);
		PR = new PacketRegistrie();
		PD.registerPacketHandler(PR);
	}

	@SuppressWarnings("unchecked")
	public BasicMessageHandler(IPacketRegistrie packetRegistrie){
		PR = packetRegistrie;
		PD = new PacketDistributor(this);
		PD.registerPacketHandler(PR);
	}

	public BasicMessageHandler(IPacketDistributor packetDistributor){
		this(new PacketRegistrie(),packetDistributor);
	}

	@SuppressWarnings("unchecked")
	public BasicMessageHandler(IPacketRegistrie packetRegistrie,IPacketDistributor packetDistributor){
		PD = packetDistributor;
		PR = packetRegistrie;
		PD.registerPacketHandler(PR);
	}

    @Override
    public void disconnect(IChatActor a) {

        try {
            stopWorkingThread();
            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (IRServer != null) {
            IRServer.disconnect();
            IRServer = null;
        }

    }

    public void shutdown() {
            disconnect(ALL);
            stopWorkingThread();
    }

    public synchronized void startWorkingThread() {
        if(workingRunnable == null) {
            workingRunnable = new WorkingThread(this);
            workingThread = new Thread(workingRunnable);
            workingThread.start();
        }
    }

    public synchronized void stopWorkingThread(){
        if(workingRunnable!=null) {
            workingRunnable.stop();
            workingRunnable = null;
            workingThread = null;
        }
    }

    @Override
    public final void addBasicChatPanel(IBasicChatPanel BCP) {

        BCPList.add(BCP);
    }

    @Override
    public final void Message(String s) {

        startWorkingThread();
        workingRunnable.addInput(s);

    }

    @Override
    public Side getSide(){
        return side;
    }

	@Override
	public IPacketDistributor getPacketDistributor() {
		return PD;
	}

	@Override
	public IPacketRegistrie getPacketRegistrie() {
		return PR;
	}

	@Override
    public final void setEmpfaenger(IChatActor ica) {

        Target = ica;
    }

    @Override
    public final void addCommand(java.lang.Class<? extends ICommand> c) {

        try {
            ICommand com = c.newInstance();
            commandList.add(com);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public final ICommand getCommand(String text) {

        for (ICommand c : commandList) {
            if (c.getName().equals(text)) {
                return c;
            }

        }

        return null;
    }

    @Override
    public final IChatActor getActor() {

        return localActor;
    }

    @Override
    public final IChatActor getUserByName(String s) {

        for (IChatActor ica : actors) {
            if (ica.getActorName().equals(s)) {
                return ica;
            }
        }

        return null;

    }

    @Override
    public final String[] getHelpForAllCommands() {

        List<String> sList = new ArrayList<String>();

        for (ICommand ic : commandList) {
            sList.add(getHelpFromCommand(ic));
        }

        String[] sArr = new String[sList.size()];
        for (int i = 0; i < sList.size(); i++) {
            sArr[i] = sList.get(i);
        }

        return sArr;
    }

    @Override
    public final String getHelpFromCommandName(String s) {

        ICommand c = getCommand(s);
        if (c != null) {
            return getHelpFromCommand(c);
        }
        return null;
    }

    @Override
    public final String getHelpFromCommand(ICommand a) {

        String[] h = a.helpCommand();
        StringBuilder sb = new StringBuilder();
        sb.append(a.getName()).append(":\n");
        for (String s : h) {
            sb.append("\t- ");
            sb.append(s);
            sb.append("\n");
        }

        return sb.toString();

    }

    @Override
    public final void print(String s) {

        System.out.println(s);
        for (IBasicChatPanel bcp : BCPList) {
            bcp.print(s);
        }
    }

    @Override
    public final void println(String s) {

        System.out.println(s);
        for (IBasicChatPanel bcp : BCPList) {
            bcp.println(s);
        }
    }

    @Override
    public final void wipe() {
        for (IBasicChatPanel bcp : BCPList) {
            bcp.WipeLog();
        }
    }

    class WorkingThread implements Runnable {


        private boolean keepGoing = true;

        public void stop() {
            keepGoing = false;
        }

        private final BasicMessageHandler basicMessageHandler;
        private final LinkedList<String> toProcess = new LinkedList<String>();

        public WorkingThread(BasicMessageHandler bmh) {
            basicMessageHandler = bmh;
        }

        public void addInput(String s) {
            toProcess.add(s);
        }

        public void run() {
            String s;
            do {
                if (!toProcess.isEmpty()) {

                    s = toProcess.pollFirst();

                    if (s.startsWith("/")) {

                        String[] strA = s.split(" ");
                        strA[0] = strA[0].replace("/", "");
                        ICommand c = getCommand(strA[0]);

                        if (c != null) {
                            if (side == Side.SERVER) {
                                c.runCommand(s,basicMessageHandler);
                                //c.runCommandServer(s, basicMessageHandler, SERVER);
                            } else if (side == Side.CLIENT) {
                                c.runCommand(s,basicMessageHandler);
                                //c.runCommandClient(s, basicMessageHandler);
                            }
                        } else {
                            println( side.name() + " : Please enter a valid command!");
                        }

                    } else {

                        setEmpfaenger(ALL);

                        if (side == Side.SERVER) {
                            sendPackage(new ChatPacket(s, SERVER.getActorName()));
                            println(getActor().getActorName() + " : " + s);
                        }
                        if (side == Side.CLIENT) {
                            sendPackage(new ChatPacket(s,getActor().getActorName()));
                        }
                    }
                }
            } while (keepGoing);
        }
    }
}