package bb.chat.command;

import bb.chat.enums.Side;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.ICommandRegistry;
import bb.chat.interfaces.IConnectionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 30.01.2015.
 */
public class BasicCommandRegistry implements ICommandRegistry {

	private java.util.List<ICommand> commandList = new ArrayList<>();

	@Override
	public final void addCommand(java.lang.Class<? extends ICommand> c) {

		try {
			ICommand com = c.newInstance();
			commandList.add(com);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public final ICommand getCommand(String text) {

		for(ICommand c : commandList) {
			if(text.equals(c.getName())) {
				return c;
			}

		}
		return null;
	}

	@Override
	public boolean runCommand(String commandLine, Side s, IConnectionHandler ich) {
		String[] strA = commandLine.split(" ");
		strA[0] = strA[0].replace("/", "");
		ICommand c = getCommand(strA[0]);

		if(c != null) {
			if(s == Side.SERVER) {
				c.runCommand(commandLine, ich);
			} else {
				c.runCommand(commandLine, ich);
			}
			return true;
		}

		ich.println("[" + ich.getActor().getActorName() + "]Please enter a valid command!");
		return false;

	}

	@Override
	public final String[] getHelpForAllCommands() {

		List<String> sList = new ArrayList<>();

		for(ICommand ic : commandList) {
			sList.add(getHelpFromCommand(ic));
		}

		String[] sArr = new String[sList.size()];
		for(int i = 0; i < sList.size(); i++) {
			sArr[i] = sList.get(i);
		}

		return sArr;
	}

	@Override
	public final String getHelpFromCommandName(String s) {

		ICommand c = getCommand(s);
		if(c != null) {
			return getHelpFromCommand(c);
		}
		return null;
	}

	@Override
	public final String getHelpFromCommand(ICommand a) {

		String[] h = a.helpCommand();
		StringBuilder sb = new StringBuilder();
		sb.append(a.getName()).append(":\n");
		for(String s : h) {
			sb.append("\t- ");
			sb.append(s);
			sb.append("\n");
		}

		return sb.toString();

	}
}
