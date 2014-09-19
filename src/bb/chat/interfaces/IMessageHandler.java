package bb.chat.interfaces;


import bb.chat.enums.Side;

import java.util.ArrayList;
import java.util.List;

/**
 * @author BB20101997
 */
@SuppressWarnings("javadoc")
public interface IMessageHandler<T, P extends IPermission<T>, G extends IUserPermissionGroup<T, P, G>> {

	IIOHandler ALL = new IIOHandler() {
		@Override
		public void start() {

		}

		@Override
		public void stop() {

		}

		@Override
		public boolean isDummy() {
			return true;
		}

		@Override
		public String getActorName() {
			return null;
		}

		@Override
		public void setActorName(String name) {

		}

		@Override
		public boolean sendPacket(IPacket p) {
			return false;
		}

		@Override
		public boolean isAlive() {
			return false;
		}

		@Override
		public void receivedHandshake() {

		}

		@Override
		public IUserPermission getUserPermission() {
			return new IUserPermission() {
				@Override
				public void addPermission(IPermission perm) {

				}

				@Override
				public void removePermission(IPermission perm) {

				}

				@Override
				public void addDeniedPermission(IPermission perm) {

				}

				@Override
				public void removeDeniedPermission(IPermission perm) {

				}

				@Override
				public List<String> getContainedGroups() {
					return null;
				}

				@Override
				public void addUserPermissionGroup(String name) {

				}

				@Override
				public void removeUserPermissionGroup(String name) {

				}

				@Override
				public List getPermissions() {
					return null;
				}

				@Override
				public List getDeniedPermissions() {
					return null;
				}
			};
		}

		@Override
		public void run() {

		}
	};

	IIOHandler SERVER = new IIOHandler() {

		IUserPermission iup = new IUserPermission() {

			List<IPermission> permi = new ArrayList<IPermission>();

			@Override
			public void addPermission(IPermission perm) {
				permi.add(perm);
			}

			@Override
			public void removePermission(IPermission perm) {

			}

			@Override
			public void addDeniedPermission(IPermission perm) {

			}

			@Override
			public void removeDeniedPermission(IPermission perm) {

			}

			@Override
			public List<String> getContainedGroups() {
				return null;
			}

			@Override
			public void addUserPermissionGroup(String name) {

			}

			@Override
			public void removeUserPermissionGroup(String name) {

			}

			@Override
			public List getPermissions() {
				return permi;
			}

			@Override
			public List getDeniedPermissions() {
				return new ArrayList();
			}
		};

		@Override
		public void start() {

		}

		@Override
		public void stop() {

		}

		@Override
		public boolean isDummy() {
			return true;
		}

		@Override
		public String getActorName() {
			return "SERVER";
		}

		@Override
		public void setActorName(String name) {

		}

		@Override
		public boolean sendPacket(IPacket p) {
			return false;
		}

		@Override
		public boolean isAlive() {
			return false;
		}

		@Override
		public void receivedHandshake() {

		}

		@Override
		public IUserPermission getUserPermission() {
			return iup;
		}

		@Override
		public void run() {

		}
	};

	IIOHandler getUserByName(String s);

	void setEmpfaenger(IIOHandler ica);

	Side getSide();

	IPacketRegistrie getPacketRegistrie();

	IPermissionRegistrie<T, P, G> getPermissionRegistry();

	IPacketDistributor getPacketDistributor();

	String getHelpFromCommand(ICommand a);

	String getHelpFromCommandName(String s);

	String[] getHelpForAllCommands();

	// messages entered by the user should land here
	void Message(String s);

	void receivePackage(IPacket p, IIOHandler sender);

	void sendPackage(IPacket p);

	// adds a Command
	void addCommand(Class<? extends ICommand> c);

	/**
	 * @param text the text entered
	 *
	 * @return the command instance matching the text
	 */
	ICommand getCommand(String text);

	// adds a BasicChatPanel to the Output�s
	void addBasicChatPanel(IBasicChatPanel BCP);

	// print to all local outputs
	void print(String s);

	// println to all local outputs
	void println(String s);

	// disconnect the connection to a
	void disconnect(IIOHandler a);

	void shutdown();

	// connects to the host at the port port
	void connect(String host, int port);

	// gets the local ChatActor
	IIOHandler getActor();

	//Will whip the chat log
	public void wipe();

}
