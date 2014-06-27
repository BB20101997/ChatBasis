package bb.chat.interfaces;

import bb.chat.gui.BasicChatPanel;

/**
 * @author BB20101997
 */
@SuppressWarnings("javadoc")
public interface IMessageHandler
{
	// get the help for all Commands and passes them down

	void helpAll(IChatActor sender);

	// TODO:Re-work the help
	void help(String s, IChatActor sender);

	void help(String[] s, IChatActor sender);

	void help(ICommand ic, IChatActor sender);

	// processes form Messages coming form somewhere else
	void recieveMessage(String s, IChatActor ica);

	// messages entered by the user should land here
	void Message(String s, IChatActor sender);

	// messages landing here will be send away
	void sendMessage(String text, String Empf, IChatActor Send);

	void sendMessageAll(String text, IChatActor Send);

	// adds a Command
	void addCommand(Class<? extends ICommand> c);

	/**
	 * @param text
	 *            the text entered
	 * @return the command instance matching the text
	 */
	ICommand getCommand(String text);

	// adds a BasicChatPanel to the Output´s
	void addBasicChatPanel(BasicChatPanel BCP);

	// print to all local outputs
	void print(String s);

	// println to all local outputs
	void println(String s);

	// disconnect the connection to a
	void disconnect(IChatActor a);

	// connects to the host at the port port
	void connect(String host, int port);

	// gets the local ChatActor
	IChatActor getActor();
}
