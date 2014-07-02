package bb.chat.interfaces;

import bb.chat.gui.BasicChatPanel;

/**
 * @author BB20101997
 */
@SuppressWarnings("javadoc")
public interface IMessageHandler
{

	public static final IChatActor	SERVER	= new IChatActor(){

												@Override
												public void setActorName(String s)
												{

												}

												@Override
												public String getActorName()
												{

													return "SERVER";
												}

												@Override
												public void disconnect()
												{

													// TODO Auto-generated
													// method stub

												}
											};

	public static final IChatActor	ALL		= new IChatActor(){

												@Override
												public void setActorName(String s)
												{

												}

												@Override
												public String getActorName()
												{

													return "ALL";
												}

												@Override
												public void disconnect()
												{

													// TODO Auto-generated
													// method stub

												}
											};

	public IChatActor getUserByName(String s);

	void setEmpfaenger(IChatActor ica);

	public String getHelpFromCommand(ICommand a);

	public String getHelpFromCommandName(String s);

	public String[] getHelpForAllCommands();

	// processes form Messages coming form somewhere else
	void recieveMessage(String s, IChatActor ica);

	// messages entered by the user should land here
	void Message(String s);

	// messages landing here will be send away
	void sendMessage(String text, IChatActor Send);

	// sends the command s with para as parameter to empf
	void sendCommand(ICommand d, IChatActor empf, String para);

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
