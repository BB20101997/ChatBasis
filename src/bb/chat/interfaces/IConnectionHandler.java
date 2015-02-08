package bb.chat.interfaces;


import bb.chat.enums.NetworkState;
import bb.chat.enums.ServerStatus;
import bb.chat.enums.Side;
import bb.chat.security.BasicPermissionRegistrie;
import bb.chat.security.BasicUser;
import bb.chat.security.BasicUserDatabase;

/**
 * @author BB20101997
 */
@SuppressWarnings("javadoc")
public interface IConnectionHandler<UD extends BasicUserDatabase,PR extends BasicPermissionRegistrie>{

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
		public boolean setActorName(String name) {
			return false;
		}

		@Override
		public boolean sendPacket(APacket p) {
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
		public NetworkState getNetworkState() {
			return NetworkState.POST_HANDSHAKE;
		}

		@Override
		public boolean isLoggedIn() {
			return false;
		}

		@Override
		public BasicUser getUser() {
			return new BasicUser();
		}

		@Override
		public void setUser(BasicUser u) {

		}

		@Override
		public void run() {

		}
	};

	IIOHandler SERVER = new IIOHandler() {

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
		public boolean setActorName(String name) {
			return false;
		}

		@Override
		public boolean sendPacket(APacket p) {
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
		public NetworkState getNetworkState() {
			return NetworkState.MANAGEMENT;
		}

		@Override
		public boolean isLoggedIn() {
			return false;
		}

		@Override
		public BasicUser getUser() {
			BasicUser b = new BasicUser();
			b.setUserID(-1);
			b.addUserPermission("*");
			return b;
		}

		@Override
		public void setUser(BasicUser u) {

		}

		@Override
		public void run() {

		}
	};

	IIOHandler getConnectionByName(String s);

	IChat<UD, PR> getIChatInstance();

	void setIChatInstance(IChat<UD, PR> ic);

	void initiate();

	// messages entered by the user should land here
	void Message(String s);

	void sendPackage(APacket p, IIOHandler target);

	// print to all local outputs
	void print(String s);

	// println to all local outputs
	void println(String s);

	// disconnect the connection to a
	void disconnect(IIOHandler a);

	Side getSide();

	// connects to the host at the port port
	boolean connect(String host, int port);

	// gets the local ChatActor
	IIOHandler getActor();

	//Will whip the chat log
	public void wipe();

	String[] getActiveUserList();

	void setActiveUsers(String[] sArgs);

	void addActiveUser(String name);

	void removeActiveUser(String name);

	int getOnlineUsers();

	void setOnlineUsers(int i);

	int getMaxUsers();

	void setMaxUsers(int i);

	String getServerName();

	void setServerName(String s);

	String getServerMessage();

	void setServerMessage(String msg);

	ServerStatus getServerStatus();

	void setServerStatus(ServerStatus status);

}
