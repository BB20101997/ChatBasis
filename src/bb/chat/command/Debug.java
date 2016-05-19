package bb.chat.command;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;

/**
 * Created by BB20101997 on 19. Mai. 2016.
 */
public class Debug implements ICommand {
	@Override
	public String getName() {
		return "debug";
	}

	@Override
	public void runCommand(String commandLine, IChat iChat) {
		iChat.getBasicChatPanel().println("This is Debug!");
	}

	@Override
	public boolean isDebugModeOnly() {
		return true;
	}
}
