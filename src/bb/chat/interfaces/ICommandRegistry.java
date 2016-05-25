package bb.chat.interfaces;

import bb.net.enums.Side;
import com.sun.istack.internal.Nullable;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by BB20101997 on 30.01.2015.
 */
public interface ICommandRegistry {

	void addCommand(Class<? extends ICommand> com);

	/**
	 * @param text the text entered
	 *
	 * @return the command instance matching name or alias to the text
	 */
	ICommand getCommand(String text);

	//evaluated in put and run ggf. command
	@SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
	boolean runCommand(String commandLine, Side side, IChat ich);

	//get the Help Message from the command
	String getHelpFromCommand(ICommand a);

	//get Helpmessage from command by alias or name
	@Nullable
	default String getHelpFromCommandName(String s){
		ICommand command = getCommand(s);
		if(command != null) {
			return getHelpFromCommand(command);
		}
		return null;
	}

	//get Helpmessage for all commands
	List<String> getHelpForAllCommands();

	List<ICommand> getAllCommands();

	/**
	 * @param s    The whole input text
	 * @param pos  The caret position in the text
	 * @param tabs How many times the auto-complete key has been pressed
	 *
	 * @return the new input-text already containing the auto-complete
	 * if no auto-complete is available just return s
	 */

	@SuppressWarnings("HardcodedFileSeparator")
	default String complete(final String s,final int caret,final int tabs){
		if(s.isEmpty()){
			return s;
		}
		ICommand iCommand = getCommand(s.substring(0,caret).split(" ")[0].replace("/",""));
		if(iCommand!=null){
			return iCommand.complete(s,caret,tabs);
		}
		else{

			String preCaret = s.substring(1, caret);
			String[] preCaretArgs = preCaret.split(" ");
			System.err.println("Found no matching Command PreCaret:"+preCaret);
			if(preCaretArgs.length == 1) {
				Stream<String> stream = getAllCommands().stream().map(ICommand::getName).filter(e -> e.startsWith(preCaret));
				String[] strings = stream.toArray(String[]::new);
				if(strings.length>0) {
					return "/" + strings[(tabs - 1) % strings.length];
				}
			}
		}
		return s;
	}
}
