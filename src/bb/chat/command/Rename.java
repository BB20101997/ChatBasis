package bb.chat.command;

import bb.chat.interfaces.IIOHandler;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IMessageHandler;
import bb.chat.network.Side;
import bb.chat.network.packet.Chatting.ChatPacket;

/**
 * @author BB20101997
 */
public class Rename implements ICommand
{

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
	public String getName()
	{

		return "rename";
	}

    @Override
    public boolean runCommand(String commandLine, IMessageHandler imh) {

        if(imh.getSide()==Side.CLIENT){
            String[] dS = commandLine.split(" ");
            if(dS.length <= 2) { return false; }

            imh.setEmpfaenger(IMessageHandler.ALL);
            imh.sendPackage(new ChatPacket(commandLine,imh.getActor().getActorName()));
            return true;
        }
        else{
            String[] dS = commandLine.split(" ");
            if(dS.length > 2)
            {
                IIOHandler ica = imh.getUserByName(dS[1]);
                if(ica != null)
                {
                    imh.getActor().setActorName(dS[2]);
                    imh.println(dS[1] + " is now known as " + dS[2]);
                    imh.setEmpfaenger(IMessageHandler.ALL);
                    imh.sendPackage(new ChatPacket(commandLine, imh.getActor().getActorName()));
                    return true;
                }

            }
            return false;
        }
    }



	@Override
	public String[] helpCommand()
	{

		return new String[ ]{"/rename <new Name>", "Used to rename you in Chat!"};
	}

    @Override
    public boolean debugModeOnly() {
        return false;
    }

	public boolean runCommandReceivedFromServer(String d, IMessageHandler a)
	{

		String[] dS = d.split(" ");
        a.println(d);
		if(dS.length > 2)
		{
            a.println(dS[1]+" is now known as "+dS[2]);
			IIOHandler ica = a.getUserByName(dS[1]);
			if(ica != null)
			{
				ica.setActorName(dS[2]);
			}
			else if(a.getActor().getActorName().equals(dS[1]))
			{
				a.getActor().setActorName(dS[2]);
				a.println("You are now known as : " + dS[2]);
			}

		}
		return true;
	}
}
