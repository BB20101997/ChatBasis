package bb.chat.interfaces;


import bb.chat.network.Side;

/**
 * @author BB20101997
 */
@SuppressWarnings("javadoc")
public interface IMessageHandler {

    IChatActor SERVER = new IChatActor() {

        @Override
        public void setActorName(String s) {

        }

        @Override
        public String getActorName() {

            return "SERVER";
        }

        @Override
        public void disconnect() {

            // TODO Auto-generated
            // method stub

        }
    };

    IChatActor ALL = new IChatActor() {

        @Override
        public void setActorName(String s) {

        }

        @Override
        public String getActorName() {

            return "ALL";
        }

        @Override
        public void disconnect() {

            // TODO Auto-generated
            // method stub

        }
    };

    IChatActor getUserByName(String s);

    void setEmpfaenger(IChatActor ica);

    Side getSide();

    IPacketRegistrie getPacketRegistrie();

	IPacketDistributor getPacketDistributor();

    String getHelpFromCommand(ICommand a);

    String getHelpFromCommandName(String s);

    String[] getHelpForAllCommands();

    // messages entered by the user should land here
    void Message(String s);

   void receivePackage(IPacket p,IChatActor sender);

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
    void disconnect(IChatActor a);

    void shutdown();

    // connects to the host at the port port
    void connect(String host, int port);

    // gets the local ChatActor
    IChatActor getActor();

    //Will whip the chat log
    public void wipe();
}
