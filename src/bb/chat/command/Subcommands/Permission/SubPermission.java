package bb.chat.command.subcommands.permission;

import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.chatting.ChatPacket;
import bb.chat.network.packet.command.PermissionPacket;
import bb.chat.security.BasicUser;
import bb.net.enums.Side;
import bb.net.interfaces.APacket;
import bb.net.interfaces.IIOHandler;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by BB20101997 on 15.12.2014.
 */
public abstract class SubPermission implements ICommand {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(SubPermission.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile("ChatBasis")));
	}


	private final String name;
	protected final List<String> perms = new ArrayList<>();

	public SubPermission(String name) {
		this.name = "permission-" + name;
		log.fine("Creating SubPermissionCommand:"+this.name);
	}

	public SubPermission(String name,String...args){
		this(name);
		perms.addAll(Arrays.asList(args));
	}

	@Override
	public final void runCommand(String commandLine, IChat iChat) {
		if(iChat.getIConnectionManager().getSide() == Side.CLIENT) {
			runClient(commandLine, iChat);
		} else {
			runServer(commandLine, iChat);
		}
	}

	protected void runServer(String cL, IChat iChat) {
		log.fine("Running command:"+name+" on the Server!");
		executePermissionCommand(iChat,iChat.getIConnectionManager().SERVER(),cL);
	}

	protected void runClient(String cL, IChat iChat) {
		log.fine("Running command:" + name + " on the Client!");
		APacket p = new PermissionPacket(cL);
		iChat.getIConnectionManager().sendPackage(p, iChat.getIConnectionManager().SERVER());
	}

	@Override
	public String getName() {
		return name;
	}

	protected boolean checkPerm(BasicUser bu,IChat iChat){
		return iChat.getPermissionRegistry().hasPermission(bu,perms);
	}

	protected ChatPacket missingPermsPacket(IChat iChat){
		return new ChatPacket("You don't have sufficent Permissions!",iChat.getLOCALActor().getActorName());
	}


	public abstract void executePermissionCommand(IChat iChat, IIOHandler executor, String cmd);

}
