package bb.chat.interfaces;


import bb.chat.network.Side;

/**
 * @author BB20101997
 */
@SuppressWarnings("javadoc")
public interface IMessageHandler {

	IIOHandler ALL = new IIOHandler() {
		@Override
		public void start() {

		}

		@Override
		public void stop() {

		}

		@Override
		public boolean isDummy() {
			return true;
		}

		@Override
		public String getActorName() {
			return null;
		}

		@Override
		public void setActorName(String name) {

		}

		@Override
		public boolean sendPacket(IPacket p) {
			return false;
		}

		@Override
		public boolean isAlive() {
			return false;
		}

		@Override
		public void receivedHandshake() {

		}

		@Override
		public void run() {

		}
	};

	IIOHandler SERVER = new IIOHandler() {
		@Override
		public void start() {

		}

		@Override
		public void stop() {

		}

		@Override
		public boolean isDummy() {
			return true;
		}

		@Override
		public String getActorName() {
			return "SERVER";
		}

		@Override
		public void setActorName(String name) {

		}

		@Override
		public boolean sendPacket(IPacket p) {
			return false;
		}

		@Override
		public boolean isAlive() {
			return false;
		}

		@Override
		public void receivedHandshake() {

		}

		@Override
		public void run() {

		}
	};

    IIOHandler getUserByName(String s);

    void setEmpfaenger(IIOHandler ica);

    Side getSide();

    IPacketRegistrie getPacketRegistrie();

	IPacketDistributor getPacketDistributor();

    String getHelpFromCommand(ICommand a);

    String getHelpFromCommandName(String s);

    String[] getHelpForAllCommands();

    // messages entered by the user should land here
    void Message(String s);

   void receivePackage(IPacket p,IIOHandler sender);

    void sendPackage(IPacket p);

    // adds a Command
    void addCommand(Class<? extends ICommand> c);

    /**
     * @param text the text entered
     * @return the command instance matching the text
     */
    ICommand getCommand(String text);

    // adds a BasicChatPanel to the Output�s
    void addBasicChatPanel(IBasicChatPanel BCP);

    // print to all local outputs
    void print(String s);

    // println to all local outputs
    void println(String s);

    // disconnect the connection to a
    void disconnect(IIOHandler a);

    void shutdown();

    // connects to the host at the port port
    void connect(String host, int port);

    // gets the local ChatActor
    IIOHandler getActor();

    //Will whip the chat log
    public void wipe();

}
