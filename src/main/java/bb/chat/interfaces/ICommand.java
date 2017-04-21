package bb.chat.interfaces;

import bb.chat.enums.Bundles;

import static bb.chat.basis.BasisConstants.EMPTY_STRING_ARRAY;

/**
 * @author BB20101997
 */
public interface ICommand {

	@SuppressWarnings("HardcodedFileSeparator")
	String COMMAND_INIT_STRING = Bundles.COMMAND.getString("init_string");

	default String[] getAlias(){
		return EMPTY_STRING_ARRAY;
	}

	/**
	 * @return Name of the command
	 */
	String getName();

	default void runCommand(String commandLine, IChat iChat){
		iChat.getBasicChatPanel().println(Bundles.COMMAND.getResource().getString("default.NYI"));
	}

	default String[] helpCommand(){
		return new String[]{Bundles.COMMAND.getResource().getString("helptext.default")};
	}

	/**
	 * @param s the text that shall be completed
	 * @param caret the caret position in s
	 * @param tabs the amount of times the auto-complete key has been pressed >=0
	 *
	 * @return the string that replaces s
	 * */
	@SuppressWarnings("StringConcatenation")
	default String complete(String s, int caret, int tabs){
		//noinspection HardcodedFileSeparator
		if(s.substring(0,caret).equals("/"+getName()+" test")){
			return s.substring(0,caret)+"-"+tabs;
		}
		return s;
	}

	//should this only be available in debug Mode
	@SuppressWarnings("PublicMethodWithoutLogging")
	default boolean isDebugModeOnly(){
		return false;
	}
}
