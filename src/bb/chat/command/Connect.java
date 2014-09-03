package bb.chat.command;

import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IMessageHandler;
import bb.chat.network.Side;

/**
 * @author BB20101997
 */
public class Connect implements ICommand
{

    @Override
    public int maxParameterCount() {
        return 2;
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
	public String getName()
	{
		return "connect";
	}

    @Override
    public boolean runCommand(String commandLine, IMessageHandler imh) {
        if(imh.getSide()== Side.CLIENT){
            String[] strA = commandLine.split(" ");
            imh.wipe();
            if(strA.length >= 3)
            {
                imh.connect(strA[1], Integer.valueOf(strA[2]));
                return true;
            }
            else if(strA.length >= 2)
            {
                imh.connect(strA[1], 256);
                return true;
            }
            else
            {
                imh.connect("192.168.178.21",256);
                return true;
            }
        }
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
