package bb.chat.command;

import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IMessageHandler;
import bb.chat.network.Side;
import bb.chat.network.packet.Chatting.ChatPacket;

/**
 * @author BB20101997
 */

public class Whisper implements ICommand
{

    @Override
    public int maxParameterCount() {
        return -1;
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
	public String getName()
	{
		return "whisper";
	}

    @Override
    public boolean runCommand(String commandLine, IMessageHandler imh) {

        if(imh.getSide()== Side.CLIENT){
            if((commandLine.split(" ", 3).length <= 2)) { return false; }
            imh.setEmpfaenger(IMessageHandler.SERVER);
            imh.sendPackage(new ChatPacket(commandLine,imh.getActor().getActorName()));
            return true;
        }
        else{
            String str[] = commandLine.split(" ", 3);
            if(str.length > 2)
            {
                System.out.println(str[1] + " : " + str[2]);
                imh.setEmpfaenger(imh.getUserByName(str[1]));
                imh.sendPackage(new ChatPacket(str[2], imh.getActor().getActorName()));
                return true;
            }
            return false;
        }
    }

    @Override
	public String[] helpCommand()
	{

		return new String[ ]{"Usage : /whisper <ToPlayer> <Message>"};
	}

    @Override
    public boolean debugModeOnly() {
        return false;
    }

}
