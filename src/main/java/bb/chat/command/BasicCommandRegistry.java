package bb.chat.command;

import bb.chat.enums.Bundles;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.ICommandRegistry;
import bb.net.enums.Side;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static bb.chat.basis.BasisConstants.LOG_NAME;

/**
 * Created by @author BB20101997 on 30.01.2015.
 */
@SuppressWarnings({"HardcodedFileSeparator", "unused"})
public class BasicCommandRegistry implements ICommandRegistry {

	private final List<ICommand> commandList = new ArrayList<>();

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(BasicCommandRegistry.class.getName());
		//noinspection DuplicateStringLiteralInspection
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}

	public final void addCommand(ICommand command){
		log.fine(MessageFormat.format(Bundles.LOG_TEXT.getResource().getString("log.command.add.one"),command.getName()));
		commandList.add(command);
	}

	@Override
	public final ICommand getCommand(String text) {
		log.finer(MessageFormat.format(Bundles.LOG_TEXT.getResource().getString("log.command.get.one"),text));
		//name is prioritised over alias
		//check for name first
		for(ICommand command : commandList) {
			if(text.equals(command.getName())) {
				return command;
			}
		}
		
		//As service loaded Commands are secondary
		List<ICommand> commands = CommandService.getInstance().getAllCommands();
		Optional<ICommand> optCom = commands.stream().filter(com->com.getName().equals(text)).findFirst();
		if(optCom.isPresent()){
			return optCom.get();
		}
		//check for alias afterwards
		for(ICommand command : commandList) {
			for(String alias : command.getAlias()) {
				if(text.equals(alias)) {
					return command;
				}
			}
		}
		
		return commands.stream().filter(com -> Arrays.stream(com.getAlias()).anyMatch(s -> s.equals(text))).findFirst().orElse(null);
	}

	@Override
	public boolean runCommand(String commandLine, Side side, IChat ich) {
		String[] strA = commandLine.split(" ");
		strA[0] = strA[0].replace(ICommand.COMMAND_INIT_STRING, "");
		ICommand command = getCommand(strA[0]);

		log.fine(MessageFormat.format(Bundles.LOG_TEXT.getString("log.command.execute"), strA[0], side.toString()));

		if(command != null) {
			if(command.isDebugModeOnly() && !ich.isDebugMode()) {
				ich.getBasicChatPanel().println(Bundles.MESSAGE.getResource().getString("debug.only"));
				return false;
			} else {
				if(side == Side.SERVER) {
					command.runCommand(commandLine, ich);
				} else {
					command.runCommand(commandLine, ich);
				}
				return true;
			}
		} else {
			log.fine(MessageFormat.format(Bundles.LOG_TEXT.getString("log.command.invalid"),strA[0]));
			ich.getBasicChatPanel().println(MessageFormat.format(Bundles.MESSAGE.getResource().getString("invalid.command"), ich.getLOCALActor().getActorName()));
			return false;
		}
	}

	@Override
	public final List<String> getHelpForAllCommands() {
		log.fine(Bundles.LOG_TEXT.getString("log.command.help.all"));
		return commandList.stream().map(this::getHelpFromCommand).collect(Collectors.toList());

	}

	@Override
	public List<ICommand> getAllCommands() {
		log.fine(Bundles.LOG_TEXT.getResource().getString("log.command.get.all"));
		List<ICommand> list = new ArrayList<>(commandList);
		list.addAll(CommandService.getInstance().getAllCommands());
		return list;
	}

	@Override
	public final String getHelpFromCommand(ICommand a) {
		log.fine(MessageFormat.format(Bundles.LOG_TEXT.getString("log.command.help.one"),a.getName()));
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
