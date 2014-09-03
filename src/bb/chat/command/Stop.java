package bb.chat.command;

import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IMessageHandler;
import bb.chat.network.Side;
import bb.chat.network.packet.Command.StopPacket;

/**
 * Created by BB20101997 on 30.08.2014.
 */
public class Stop implements ICommand {
    @Override
    public int maxParameterCount() {
        return 0;
    }

    @Override
    public int minParameterCount() {
        return 0;
    }

    @Override
    public String[] getAlias() {
        return new String[0];
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public boolean runCommand(String commandLine, IMessageHandler imh) {
        if (imh.getSide() == Side.CLIENT) {
            imh.sendPackage(new StopPacket());
            return false;
        } else {
            imh.shutdown();
            return true;
        }
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
