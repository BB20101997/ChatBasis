package bb.chat.interfaces;

import bb.chat.base.Constants;
import bb.net.enums.Side;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Created by BB20101997 on 30.01.2015.
 */
public interface ICommandRegistry {

	@SuppressWarnings("ConstantNamingConvention")
	Logger log = Constants.getLogger(ICommandRegistry.class);

	@Deprecated
	default void addCommand(Class<? extends ICommand> com){
		try {
			addCommand(com.getConstructor().newInstance());
		} catch(Exception e) {
			e.printStackTrace();
			//noinspection StringConcatenationMissingWhitespace
			log.warning("Error while adding Command." + System.lineSeparator() + "Command may not be available.");
		}
	}

	void addCommand(ICommand command);

	/**
	 * @param text the name or alias of the command to return
	 *
	 * @return the command instance matching name or alias to the text or null if not found
	 */
	ICommand getCommand(String text);

	default boolean isCommandAvailable(String name){
		return getCommand(name)!=null;
	}

	//evaluated in put and run ggf. command
	@SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
	boolean runCommand(String commandLine,Side side, IChat ich);

	//get the Help Message from the command
	String getHelpFromCommand(ICommand a);

	//get Helpmessage from command by alias or name
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
	 * @param caret The caret position in the text
	 * @param tabs How many times the auto-complete key has been pressed
	 *
	 * @return the new input-text already containing the auto-complete
	 * if no auto-complete is available just return s
	 */

	@SuppressWarnings({"HardcodedFileSeparator", "MethodWithMultipleReturnPoints"})
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
			//System.err.println("Found no matching Command PreCaret:"+preCaret);
			if(preCaretArgs.length == 1) {
				Stream<String> stream = getAllCommands().stream().map(ICommand::getName).filter(e -> e.startsWith(preCaret));
				String[] strings = stream.toArray(String[]::new);
				if(strings.length>0) {
					//noinspection StringConcatenation
					return "/" + strings[(tabs - 1) % strings.length];
				}
			}
		}
		return s;
	}
}
