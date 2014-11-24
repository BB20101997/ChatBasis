package bb.chat.interfaces;

/**
 * @author BB20101997
 */
public interface ICommand {

	// -1 for no max
	int maxParameterCount();

	int minParameterCount();

	String[] getAlias();

	/**
	 * @return Name of the Command
	 */
	String getName();

	/**
	 * Replacing the old runCommand you can get the IChatActor by calling : imh.getActor(); It should be the same!
	 */
	boolean runCommand(String commandLine, IMessageHandler imh);

	String[] helpCommand();

	boolean debugModeOnly();
}