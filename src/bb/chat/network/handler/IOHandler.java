package bb.chat.network.handler;

import bb.chat.enums.NetworkState;
import bb.chat.enums.Side;
import bb.chat.interfaces.IIOHandler;
import bb.chat.interfaces.IMessageHandler;
import bb.chat.interfaces.IPacket;
import bb.chat.interfaces.IUserPermission;
import bb.chat.network.packet.Command.DisconnectPacket;
import bb.chat.network.packet.DataOut;
import bb.chat.network.packet.Handshake.HandshakePacket;
import bb.chat.security.StringUserPermission;

import java.io.*;

/**
 * @author BB20101997
 */
public class IOHandler implements Runnable, IIOHandler {

	private final IMessageHandler IMH;
	private final DataInputStream dis;
	private final DataOutputStream dos;
	protected boolean handshakeReceived = false;
	private final IUserPermission userPermission;
	private String name = "NO-NAME-BUG";
	private boolean continueLoop = true;
	private Thread thread;
	protected NetworkState status = NetworkState.UNKNOWN;
	/**
	 * the higher the security the more the user can do
	 */
	protected int securityLevel = 0;


	/**
	 * @param IS  the InputStream to be used
	 * @param OS  the OutputStream to be used
	 * @param imh an IMessageHandler to be linked to
	 */

	public IOHandler(final InputStream IS, OutputStream OS, IMessageHandler imh, IUserPermission iup) {
		IMH = imh;
		System.out.println("Creating Streams");
		dis = new DataInputStream(IS);
		dos = new DataOutputStream(OS);
		status = NetworkState.HANDSHAKE;
		if(imh.getSide() == Side.CLIENT) {
			startHandshake();
		} else {
			sendPacket(imh.getPacketRegistrie().getSyncPacket());
		}
		userPermission = iup;
	}

	public IOHandler(final InputStream IS, OutputStream OS, IMessageHandler imh) throws IOException {
		this(IS, OS, imh, new StringUserPermission());
	}

	private class handshakeRunnable implements Runnable {
		public handshakeRunnable() {

		}

		public final Object obj = new Object();

		@Override
		public void run() {
			for(int i = 0; !handshakeReceived || i > 900000; i++) {
				try {
					synchronized(obj) {
						obj.wait(10);
					}
				} catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(!handshakeReceived) {
				stop();
				System.out.println("Shutting down : No handshake!");
				status = NetworkState.SHUTDOWN;
			} else {
				status = NetworkState.POST_HANDSHAKE;
				System.out.println("Handshake Received!");
			}
		}
	}

	private void startHandshake() {
		sendPacket(new HandshakePacket());
	}

	public void start() {
		if(thread == null) {
			thread = new Thread(this);
		}
		if(thread.getState() == Thread.State.NEW) {
			thread.start();
		}
	}

	public void stop() {
		continueLoop = false;
		if(thread != null) {
			thread.interrupt();
		}
	}

	public int getSecurityLevel() {
		return securityLevel;
	}

	public void setSecurityLevel(int level) {
		securityLevel = level;
	}

	@Override
	public boolean isDummy() {
		return false;
	}


	@Override
	public void run() {

		System.out.println("Starting IOHandler");


		if(IMH.getSide() == Side.SERVER) {
			Thread t = new Thread(new handshakeRunnable());
			t.start();
		}

		int id;
		int length;

		while(continueLoop) {
			try {
				id = dis.readInt();
				length = dis.readInt();
				byte[] by = new byte[length];
				dis.readFully(by);
				System.out.println("IOHandler: PacketReceived : " + IMH.getPacketRegistrie().getPacketClassByID(id) + " on Side : " + IMH.getSide());
				IMH.getPacketDistributor().distributePacket(id, by, this);

			} catch(Exception e) {
				sendPacket(new DisconnectPacket());
				System.out.println("Exception in IOHandler, closing connection!");
				//e.printStackTrace();
				continueLoop = false;
			}
		}

		System.out.println("Stopping IOHandler");

		IMH.disconnect(this);

		status = NetworkState.SHUTDOWN;

		try {
			dis.close();
		} catch(IOException e) {
			e.printStackTrace();
		}

		try {
			dos.close();
		} catch(IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getActorName() {

		return name;
	}

	@Override
	public void setActorName(String s) {
		name = s;
	}

	@SuppressWarnings("unchecked")
	public boolean sendPacket(IPacket p) {

		if(IMH.getPacketRegistrie().containsPacket(p.getClass())) {

			int id = IMH.getPacketRegistrie().getID(p.getClass());

			DataOut dataOut = DataOut.newInstance();

			try {
				p.writeToData(dataOut);
			} catch(IOException e) {
				e.printStackTrace();
				return false;
			}

			byte[] b = dataOut.getBytes();


			try {
				dos.writeInt(id);
				dos.writeInt(b.length);
				dos.write(b);
			} catch(IOException e) {
				// e.printStackTrace();
				return false;
			}


			return true;

		}

		return false;
	}

	@Override
	public void finalize() throws Throwable {
		if(isAlive())
			stop();
		super.finalize();

	}

	/**
	 * @return if the end() method was called or the run method ended
	 */
	public boolean isAlive() {
		return continueLoop;
	}

	@Override
	public void receivedHandshake() {
		handshakeReceived = true;
	}

	@Override
	public IUserPermission getUserPermission() {
		return null;
	}

}
