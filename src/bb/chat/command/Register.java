package bb.chat.command;

import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IMessageHandler;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class Register implements ICommand {
    @Override
    public int maxParameterCount() {
        return 2;
    }

    @Override
    public int minParameterCount() {
        return 2;
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean runCommand(String commandLine, IMessageHandler imh) {
        //TODO:Implement functionality
        return false;
    }

    @Override
    public String[] helpCommand() {
        return new String[0];
    }

    @Override
    public boolean debugModeOnly() {
        return false;
    }

}
