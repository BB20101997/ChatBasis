package bb.chat.command;

import bb.chat.interfaces.ICommand;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.util.ArrayList;
import java.util.ServiceLoader;
import java.util.logging.Logger;

import static bb.chat.basis.BasisConstants.LOG_NAME;

/**
 * Created by BB20101997 on 21. Apr. 2017.
 */
public class CommandService {
    
    @SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(CommandService.class.getName());
		//noinspection DuplicateStringLiteralInspection
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}
    
    private static CommandService ourInstance = new CommandService();
    private ServiceLoader<ICommand> loader;
    
    public static CommandService getInstance() {
        return ourInstance;
    }
    
    private CommandService() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Loading ICommand Services").append('\n');
        loader = ServiceLoader.load(ICommand.class);
        for(ICommand command:getAllCommands()){
            stringBuilder.append("Loaded Command:").append(command.getClass()).append('\n');
        }
        log.info(stringBuilder.toString());
    }
    
    public java.util.List<ICommand> getAllCommands(){
        java.util.List<ICommand> ret = new ArrayList<>();
        loader.iterator().forEachRemaining(ret::add);
        return ret;
    }
}
