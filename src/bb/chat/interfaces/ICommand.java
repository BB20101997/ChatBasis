package bb.chat.interfaces;

/**
 * @author BB20101997
 */
public interface ICommand {

	String[] getAlias();

	/**
	 * @return Name of the Command
	 */
	String getName();

	/**
	 * Replacing the old runCommand you can get the IChatActor by calling : imh.getActor(); It should be the same!
	 */
	void runCommand(String commandLine, IConnectionHandler imh);

	String[] helpCommand();

	boolean debugModeOnly();
}