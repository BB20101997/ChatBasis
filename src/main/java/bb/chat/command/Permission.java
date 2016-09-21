package bb.chat.command;

import bb.chat.command.subcommands.permission.Create;
import bb.chat.command.subcommands.permission.Delete;
import bb.chat.command.subcommands.permission.Help;
import bb.chat.command.subcommands.permission.SubPermission;
import bb.chat.command.subcommands.permission.group.*;
import bb.chat.command.subcommands.permission.user.*;
import bb.chat.enums.Bundles;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static bb.chat.basis.BasisConstants.LOG_NAME;

/**
 * Created by BB20101997 on 28.11.2014.
 */
public class Permission implements ICommand {

	public final List<SubPermission> subCommandList = new ArrayList<>();

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(Permission.class.getName());
		//noinspection DuplicateStringLiteralInspection
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}


	@SuppressWarnings("OverlyCoupledMethod")
	public Permission() {
		subCommandList.add(new Help(this));
		subCommandList.add(new Create());
		subCommandList.add(new Delete());
		subCommandList.add(new bb.chat.command.subcommands.permission.List());
		subCommandList.add(new UserPermAdd());
		subCommandList.add(new UserPermRemove());
		subCommandList.add(new UserDenyAdd());
		subCommandList.add(new UserDenyRemove());
		subCommandList.add(new UserGroupAdd());
		subCommandList.add(new UserGroupRemove());
		subCommandList.add(new GroupCreate());
		subCommandList.add(new GroupDelete());
		subCommandList.add(new GroupPermAdd());
		subCommandList.add(new GroupPermRemove());
		subCommandList.add(new GroupDenyAdd());
		subCommandList.add(new GroupDenyRemove());
		subCommandList.add(new GroupAddGroup());
		subCommandList.add(new GroupRemoveGroup());
	}

	@Override
	public String[] getAlias() {
		String[] sA = new String[subCommandList.size()];
		for(int i = 0; i < sA.length; i++) {
			sA[i] = subCommandList.get(i).getName();
		}
		log.fine("Returning Alias List:" + Arrays.toString(sA));
		return sA;
	}

	@Override
	public String getName() {
		return "permission";
	}

	@Override
	public String complete(String s, int caret, int tabs) {
		String preCaret = s.substring(1, caret);
		String[] preCaretArgs = preCaret.split(" ");
		if(preCaretArgs.length == 1) {
			Stream<String> stream = subCommandList.stream().map(SubPermission::getName).filter(e -> e.startsWith(preCaret));
			String[] strings = stream.toArray(String[]::new);
			if(strings.length > 0) {
				//noinspection StringConcatenationMissingWhitespace,StringConcatenation
				return ICommand.COMMAND_INIT_STRING + strings[(tabs - 1) % strings.length];
			}
		}
		return s;
	}

	@Override
	public void runCommand(String commandLine, IChat iChat) {
		String[] command = commandLine.split(" ", 2);
		log.fine("Received command:" + command[0]);

		if(!getName().equals(command[0].replace(COMMAND_INIT_STRING, ""))) {
			for(SubPermission sC : subCommandList) {
				if(sC.getName().equals(command[0].replace(COMMAND_INIT_STRING, ""))) {
					log.fine("Found matching subcommand!");
					sC.runCommand(commandLine, iChat);
					break;
				}
			}
		} else {
			iChat.getBasicChatPanel().println(Bundles.MESSAGE.getResource().getString("perm.default"));
		}
	}

	@Override
	public boolean isDebugModeOnly() {
		return false;
	}
}
