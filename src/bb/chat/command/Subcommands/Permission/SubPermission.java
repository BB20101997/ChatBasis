package bb.chat.command.Subcommands.Permission;

import bb.chat.enums.Side;
import bb.chat.interfaces.ICommand;
import bb.chat.interfaces.IIOHandler;
import bb.chat.interfaces.IConnectionHandler;
import bb.chat.interfaces.APacket;
import bb.chat.network.packet.Command.PermissionPacket;

/**
 * Created by BB20101997 on 15.12.2014.
 */
public abstract class SubPermission implements ICommand{

	public final String name;
	public String[] alias = new String[0];
	public String[] help = new String[0];
	public boolean debug = false;

	public SubPermission(String name){
		this.name = "permission-"+name;
	}

	@Override
	public final void runCommand(String commandLine, IConnectionHandler imh) {
		if(imh.getSide() == Side.CLIENT){
			runClient(commandLine,imh);
		}
		else {
			runServer(commandLine, imh);
		}
	}

	protected void runServer(String cL, IConnectionHandler imh) {
		String[] command = cL.split(" ", 2);
		imh.getIChatInstance().getPermissionRegistry().executePermissionCommand(imh, IConnectionHandler.SERVER,command[0],command[1]);
		System.out.println("Run permission Sub-Command " + cL + " on Side Server");
	}

	protected void runClient(String cL, IConnectionHandler imh) {
		String[] command = cL.split(" ", 2);
		APacket p = new PermissionPacket(command[0], command[1]);
		imh.sendPackage(p, IConnectionHandler.SERVER);
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

	public abstract void executePermissionCommand(IConnectionHandler imh,IIOHandler executor,String cmd,String rest);


	@Override
	public String[] helpCommand() {
		return help;
	}

	@Override
	public boolean debugModeOnly() {
		return debug;
	}
}
