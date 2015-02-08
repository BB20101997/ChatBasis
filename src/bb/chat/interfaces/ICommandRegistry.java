package bb.chat.interfaces;

import bb.chat.enums.Side;

/**
 * Created by BB20101997 on 30.01.2015.
 */
public interface ICommandRegistry {

	void addCommand(Class<? extends ICommand> c);

	/**
	 * @param text the text entered
	 *
	 * @return the command instance matching the text
	 */
	ICommand getCommand(String text);

	boolean runCommand(String commandLine,Side s,IConnectionHandler ich);

	String getHelpFromCommand(ICommand a);

	String getHelpFromCommandName(String s);

	String[] getHelpForAllCommands();
}
