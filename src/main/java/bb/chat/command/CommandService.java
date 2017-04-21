package bb.chat.command;

/**
 * Created by BB20101997 on 21. Apr. 2017.
 */
public class CommandService {
    private static CommandService ourInstance = new CommandService();
    
    public static CommandService getInstance() {
        return ourInstance;
    }
    
    private CommandService() {
    }
}
