package bb.chat.command.subcommands.permission;

import bb.chat.basis.BasisConstants;
import bb.chat.enums.Bundles;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.IChatActor;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.command.PermissionPacket;
import bb.chat.security.BasicUser;
import bb.net.enums.Side;
import bb.net.interfaces.APacket;
import bb.net.interfaces.IIOHandler;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by BB20101997 on 15.12.2014.
 */
public abstract class SubPermission implements ICommand {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger logger           = BasisConstants.getLogger(SubPermission.class);
	public static final String  MISSING_PERM     = "perm.missing";
	public static final String  LOG_MISSING_PERM = "log.command.perm.missing";
	public static final String SUB_PERM_SEPERATOR = "-";

	protected final List<String> perms = new ArrayList<>();
	protected String subname;

	@Deprecated
	public SubPermission(String name) {
		//noinspection StringConcatenation
		this.subname =  name;
		logger.fine(MessageFormat.format(Bundles.LOG_TEXT.getString("log.command.add.sub.perm"),subname));
	}

	@Deprecated
	@SuppressWarnings("OverloadedVarargsMethod")
	public SubPermission(String name, String...args){
		this(name);
		perms.addAll(Arrays.asList(args));
	}

	public SubPermission(String...args){
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
		logger.fine("Running command:"+getName()+" on the Server!");
		executePermissionCommand(iChat,iChat.getIConnectionManager().SERVER(),cL);
	}

	protected void runClient(String cL, IChat iChat) {
		logger.fine("Running command:" + getName() + " on the Client!");
		APacket p = new PermissionPacket(cL);
		iChat.getIConnectionManager().sendPackage(p, iChat.getIConnectionManager().SERVER());
	}

	@Override
	public String getName() {
		return Bundles.COMMAND.getString("name.permission")+SUB_PERM_SEPERATOR+subname;
	}

	public String getSubName(){
		return subname;
	}

	protected boolean checkPerm(IIOHandler iioHandler, IChat iChat){
		IChatActor iChatActor = iChat.getActorByIIOHandler(iioHandler);
		if(iChatActor == null) {
			logger.fine("Couldn't get Actor!");
			return false;
		}
		BasicUser basicUser = iChatActor.getUser();
		return iChat.getPermissionRegistry().hasPermission(basicUser,perms);
	}

	protected boolean checkPerm(BasicUser bu,IChat iChat){
		return iChat.getPermissionRegistry().hasPermission(bu,perms);
	}

	public abstract void executePermissionCommand(IChat iChat, IIOHandler executor, String cmd);

}
