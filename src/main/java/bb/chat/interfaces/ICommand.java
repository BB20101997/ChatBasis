package bb.chat.interfaces;

/**
 * @author BB20101997
 */
public interface ICommand {

	@SuppressWarnings("HardcodedFileSeparator")
	char COMMAND_INIT_CHAR = '/';
	@SuppressWarnings("HardcodedFileSeparator")
	String COMMAND_INIT_STRING = "/";
	String[] NO_ALIAS = new String[0];
	String[] DEFAULT_HELP = new String[]{"No help given!"};
	String RES_NAME = "bb.chat.lang.Command";

	default String[] getAlias(){
		return NO_ALIAS;
	}

	/**
	 * @return Name of the command
	 */
	String getName();

	default void runCommand(String commandLine, IChat iChat){
		iChat.getBasicChatPanel().println("Sorry NYI");
	}

	default String[] helpCommand(){
		return DEFAULT_HELP;
	}


	default String complete(String s,int caret,int tabs){
		//noinspection HardcodedFileSeparator
		if(s.substring(0,caret).equals("/"+getName()+" test")){
			return s.substring(0,caret)+"-"+tabs;
		}
		return s;
	}

	//should this only be available in debug Mode
	default boolean isDebugModeOnly(){
		return false;
	}
}