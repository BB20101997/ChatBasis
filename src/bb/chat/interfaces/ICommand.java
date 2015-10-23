package bb.chat.interfaces;

/**
 * @author BB20101997
 */
public interface ICommand {

	@SuppressWarnings("HardcodedFileSeparator")
	char COMMAND_INIT_CHAR = '/';

	@SuppressWarnings("HardcodedFileSeparator")
	String COMMAND_INIT_STRING = "/";

	String[] getAlias();

	/**
	 * @return Name of the command
	 */
	String getName();

	void runCommand(String commandLine, IChat iChat);

	default String[] helpCommand(){
		return new String[]{"No help given!"};
	};

	//should this only be available in debug Mode
	boolean isDebugModeOnly();
}