package bb.chat.command;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.ICommandRegistry;
import bb.net.enums.Side;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by @author BB20101997 on 30.01.2015.
 */
@SuppressWarnings("HardcodedFileSeparator")
public class BasicCommandRegistry implements ICommandRegistry {

	private final List<ICommand> commandList = new ArrayList<>();

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(BasicCommandRegistry.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile("ChatBasis")));
	}
	
	@Override
	public final void addCommand(java.lang.Class<? extends ICommand> command) {

		try {
			ICommand com = command.getConstructor().newInstance();
			commandList.add(com);
		} catch(Exception e) {
			e.printStackTrace();
			//noinspection StringConcatenationMissingWhitespace
			log.warning("Error while adding Command."+System.lineSeparator()+"Command may not be available.");
		}
	}

	@Override
	public final ICommand getCommand(String text) {
	log.finer("Receiving Command named "+text);
		//name is prioritised over alias
		//check for name first
		for(ICommand command : commandList) {
			if(text.equals(command.getName())) {
				return command;
			}
		}
		//check for alias afterwards
		for(ICommand command:commandList){
			for(String alias:command.getAlias()){
				if(text.equals(alias)){
					return command;
				}
			}
		}
		return null;
	}

	@Override
	public boolean runCommand(String commandLine, Side side, IChat ich) {
		log.fine("Running "+commandLine+" on the "+side+".");
		String[] strA = commandLine.split(" ");
		strA[0] = strA[0].replace(ICommand.COMMAND_INIT_STRING, "");
		ICommand command = getCommand(strA[0]);

		if(command != null) {
			if(side == Side.SERVER) {
				command.runCommand(commandLine, ich);
			} else {
				command.runCommand(commandLine, ich);
			}
			return true;
		}
		log.fine(strA[0]+" is not a valid command!");
		ich.getBasicChatPanel().println("[" + ich.getLOCAL().getActorName() + "]Please enter a valid command!");
		return false;

	}

	@Override
	public final String[] getHelpForAllCommands() {

		List<String> sList = commandList.stream().map(this::getHelpFromCommand).collect(Collectors.toList());

		String[] sArr = new String[sList.size()];
		for(int i = 0; i < sList.size(); i++) {
			sArr[i] = sList.get(i);
		}

		return sArr;
	}

	@Override
	public final String getHelpFromCommandName(String s) {
		ICommand command = getCommand(s);
		if(command != null) {
			return getHelpFromCommand(command);
		}
		return null;
	}

	@Override
	public final String getHelpFromCommand(ICommand a) {

		String[] h = a.helpCommand();
		StringBuilder sb = new StringBuilder();
		sb.append(a.getName()).append(":").append(System.lineSeparator());
		for(String s : h) {
			sb.append("\t- ");
			sb.append(s);
			sb.append(System.lineSeparator());
		}

		return sb.toString();

	}
}
