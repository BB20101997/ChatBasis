package bb.chat.command;

import bb.chat.basis.BasisConstants;
import bb.chat.enums.Bundles;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.IChatActor;
import bb.chat.interfaces.ICommand;
import bb.chat.network.packet.command.RenamePacket;
import bb.net.enums.Side;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.text.MessageFormat;
import java.util.logging.Logger;

import static bb.chat.basis.BasisConstants.LOG_NAME;

/**
 * @author BB20101997
 */
@SuppressWarnings({"ClassNamingConvention"})
public class Rename implements ICommand {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(Rename.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}

	@Override
	public String getName() {

		return "rename";
	}

	@Override
	public void runCommand(final String commandLine,final IChat iChat) {
		log.fine("Executing rename!");
		String[] dS = commandLine.split(" ");
		//Client, SERVER and ALL are reserved names
		if(dS.length <= 2 || BasisConstants.CLIENT.equals(dS[2]) || iChat.getSERVERActor().getActorName().equals(dS[2]) || iChat.getALLActor().getActorName().equals(dS[2])) {
			return;
		}
		if(iChat.getIConnectionManager().getSide() == Side.CLIENT) {
			//if client just inform Server of the request
			iChat.getIConnectionManager().sendPackage(new RenamePacket(dS[1], dS[2]), iChat.getIConnectionManager().ALL());
		} else {
			//get the players actor
			IChatActor ica = iChat.getActorByName(dS[1]);
			if(ica != null) {
				ica.setActorName(dS[2],true);
				iChat.getBasicChatPanel().println(MessageFormat.format(Bundles.MESSAGE.getResource().getString("rename.announce"), iChat.getLOCALActor().getActorName(), dS[1], dS[2]));
				//iChat.getIConnectionManager().sendPackage(new RenamePacket(dS[1], dS[2]), iChat.getIConnectionManager().ALL());
			}
		}
	}

	@Override
	public String[] helpCommand() {
		//noinspection StringConcatenationMissingWhitespace
		return new String[]{MessageFormat.format(Bundles.COMMAND.getResource().getString("helptext.rename"),ICommand.COMMAND_INIT_STRING)};
	}
}
