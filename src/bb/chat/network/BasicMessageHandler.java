package bb.chat.network;

import java.util.ArrayList;
import java.util.List;

import bb.chat.gui.BasicChatPanel;
import bb.chat.interfaces.IChatActor;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IMessageHandler;

public abstract class BasicMessageHandler implements IMessageHandler
{

	List<IChatActor>		actors		= new ArrayList<IChatActor>();

	IChatActor				Target;

	Side					side;

	List<ICommand>			commandList	= new ArrayList<ICommand>();

	IChatActor				localActor;

	List<BasicChatPanel>	BCPList		= new ArrayList<BasicChatPanel>();

	@Override
	public final void addBasicChatPanel(BasicChatPanel BCP)
	{

		BCPList.add(BCP);
	}

	@Override
	public final void Message(String s)
	{

		if(s.startsWith("/"))
		{
			String[] strA = s.split(" ");
			strA[0] = strA[0].replace("/", "");
			ICommand c = getCommand(strA[0]);
			if(c != null)
			{
				if(side == Side.SERVER)
				{
					c.runCommandServer(s, this, SERVER);
				}
				else if(side == Side.CLIENT)
				{
					c.runCommandClient(s, this);
				}
			}
			else
			{
				println("CLIENT : Please enter a valid commans!");
			}
		}
		else
		{
			setEmpfaenger(ALL);
			if(side == Side.SERVER)
			{
				sendMessage(getActor().getActorName() + " : " + s, SERVER);
				println(getActor().getActorName() + " : " + s);
			}
			if(side == Side.CLIENT)
			{
				sendMessage(s, SERVER);
			}
		}

	}

	@Override
	public final void setEmpfaenger(IChatActor ica)
	{

		Target = ica;
	}

	@Override
	public final void addCommand(java.lang.Class<? extends ICommand> c)
	{

		try
		{
			ICommand com = c.newInstance();
			commandList.add(com);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	};

	@Override
	public final ICommand getCommand(String text)
	{

		for(ICommand c : commandList)
		{
			if(c.getName().equals(text)) { return c; }

		}

		return null;
	}

	@Override
	public final IChatActor getActor()
	{

		return localActor;
	}

	@Override
	public final IChatActor getUserByName(String s)
	{

		for(IChatActor ica : actors)
		{
			if(ica.getActorName().equals(s)) { return ica; }
		}

		return null;

	}

	@Override
	public final String[] getHelpForAllCommands()
	{

		List<String> sList = new ArrayList<String>();

		for(ICommand ic : commandList)
		{
			sList.add(getHelpFromCommand(ic));
		}

		String[] sArr = new String[sList.size()];
		for(int i = 0; i < sList.size(); i++)
		{
			sArr[i] = sList.get(i);
		}

		return sArr;
	}

	@Override
	public final String getHelpFromCommandName(String s)
	{

		ICommand c = getCommand(s);
		if(c != null) { return getHelpFromCommand(c); }
		return null;
	}

	@Override
	public final String getHelpFromCommand(ICommand a)
	{

		String[] h = a.helpCommand();
		StringBuilder sb = new StringBuilder();
		sb.append(a.getName() + ":\n");
		for(String s : h)
		{
			sb.append("\t- ");
			sb.append(s);
			sb.append("\n");
		}

		return sb.toString();

	}

	@Override
	public final void sendCommand(ICommand d, IChatActor empf, String para)
	{

		setEmpfaenger(empf);
		sendMessage("/" + d.getName() + " " + para, getActor());

	}

	@Override
	public final void print(String s)
	{

		System.out.println(s);
		for(BasicChatPanel bcp : BCPList)
		{
			bcp.print(s);
		}
	}

	@Override
	public final void println(String s)
	{

		System.out.println(s);
		for(BasicChatPanel bcp : BCPList)
		{
			bcp.println(s);
		}
	}
}
