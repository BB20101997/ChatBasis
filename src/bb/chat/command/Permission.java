package bb.chat.command;

import bb.chat.command.Subcommands.Permission.*;
import bb.chat.command.Subcommands.Permission.Group.*;
import bb.chat.command.Subcommands.Permission.Help;
import bb.chat.command.Subcommands.Permission.User.*;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IConnectionHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 28.11.2014.
 */
public class Permission implements ICommand {

	public final List<SubPermission> subCommandList = new ArrayList<>();

	public Permission() {
		subCommandList.add(new Help(this));
		subCommandList.add(new Create());
		subCommandList.add(new Delete());
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
		String[] s = new String[subCommandList.size()];
		for(int i = 0; i < s.length; i++) {
			s[i] = subCommandList.get(i).getName();
		}
		return s;
	}

	@Override
	public String getName() {
		return "permission";
	}

	@Override
	public void runCommand(String commandLine, IConnectionHandler imh) {
		String[] command = commandLine.split(" ", 3);

		if(!"permission".equals(command[0])) {
			for(SubPermission sC : subCommandList) {
				if(("permission-" + sC.getName()).equals(command[0])) {
					sC.runCommand(commandLine, imh);
					break;
				}
			}
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
