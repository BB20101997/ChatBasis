package bb.chat.command.Subcommands.Permission;

import bb.chat.command.Permission;
import bb.chat.enums.Side;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IIOHandler;
import bb.chat.interfaces.IMessageHandler;
import bb.chat.interfaces.IPacket;
import bb.chat.network.packet.Command.PermissionPacket;

/**
 * Created by BB20101997 on 15.12.2014.
 */
public abstract class SubPermission implements ICommand{

	public final String name;
	public String[] alias = new String[0];
	public String[] help = new String[0];
	public boolean debug = false;
	protected final Permission instance;

	public SubPermission(Permission instance,String name){
		this.name = "permission-"+name;
		this.instance = instance;
	}

	@Override
	public final void runCommand(String commandLine, IMessageHandler imh) {
		if(imh.getSide() == Side.CLIENT){
			runClient(commandLine,imh);
		}
		else {
			runServer(commandLine, imh);
		}
	}

	protected void runServer(String cL, IMessageHandler imh) {
		String[] command = cL.split(" ", 2);
		imh.getPermissionRegistry().executePermissionCommand(imh,IMessageHandler.SERVER,command[0],command[1]);
		System.out.println("Run permission Sub-Command " + cL + " on Side Server");
	}

	protected void runClient(String cL, IMessageHandler imh) {
		String[] command = cL.split(" ", 2);
		IPacket p = new PermissionPacket(command[0], command[1]);
		imh.sendPackage(p);
		System.out.println("Run permission Sub-Command "+cL+" on Side Client");
	}

	@Override
	public String[] getAlias() {
		return alias;
	}

	@Override
	public String getName() {
		return name;
	}

	public abstract void executePermissionCommand(IMessageHandler imh,IIOHandler executor,String cmd,String rest);


	@Override
	public String[] helpCommand() {
		return help;
	}

	@Override
	public boolean debugModeOnly() {
		return debug;
	}
}
