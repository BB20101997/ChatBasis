package bb.chat.interfaces;

import bb.net.enums.Side;

/**
 * Created by BB20101997 on 30.01.2015.
 */
public interface ICommandRegistry {

	void addCommand(Class<? extends ICommand> c);

	/**
	 * @param text the text entered
	 *
	 * @return the command instance matching name or alias to the text
	 */
	ICommand getCommand(String text);

	//evaluated in put and run ggf. command
	boolean runCommand(String commandLine, Side s, IChat ich);

	//get the Halp Message from the command
	String getHelpFromCommand(ICommand a);

	//get Helpmessage from command by alias or name
	String getHelpFromCommandName(String s);

	//get Helpmessage for all commands
	String[] getHelpForAllCommands();
}
