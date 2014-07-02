package bb.chat.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import bb.chat.interfaces.IChatActor;
import bb.chat.interfaces.IMessageHandler;

/**
 * @author BB20101997
 */
public class IOHandler implements Runnable, IChatActor
{

	List<IMessageHandler>	MessegH			= new ArrayList<IMessageHandler>();
	BufferedReader			InStream;
	PrintWriter				OutStream;
	@SuppressWarnings("unused")
	private InputStream		InputStream;
	private String			name;
	private boolean			continueLoop	= true;

	/**
	 * @param IS
	 *            the InputStream to be used
	 * @param OS
	 *            the OutputStream to be used
	 */
	public IOHandler(InputStream IS, OutputStream OS)
	{

		InStream = new BufferedReader(new InputStreamReader(IS));
		OutStream = new PrintWriter(OS);
		InputStream = IS;
	}

	/**
	 * @param IS
	 *            the InputStream to be used
	 * @param OS
	 *            the OutputStream to be used
	 * @param imh
	 *            an IMessageHandler to be linked to
	 */
	public IOHandler(InputStream IS, OutputStream OS, IMessageHandler imh)
	{

		addMessageHandler(imh);
		InStream = new BufferedReader(new InputStreamReader(IS));
		OutStream = new PrintWriter(OS);
		InputStream = IS;
	}

	/**
	 * @param IMH
	 *            adds the IMessageHandler
	 */
	public void addMessageHandler(IMessageHandler IMH)
	{

		MessegH.add(IMH);
	}

	/**
	 * @param IMH
	 *            removes the IMessageHandler
	 */
	public void removeMessageHandler(IMessageHandler IMH)
	{

		if(MessegH.contains(IMH))
		{
			MessegH.remove(IMH);
		}
	}

	@Override
	public void run()
	{

		System.out.println("Starting IOHandler");
		String text;
		main:
		while(continueLoop)
		{
			try
			{
				while(((text = InStream.readLine()) != null) && continueLoop)
				{
					System.out.println("Recieved Message : " + text);
					for(IMessageHandler IMH : MessegH)
					{
						IMH.recieveMessage(text, this);
					}

					if(!continueLoop)
					{
						break main;
					}
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
				continueLoop = false;
			}
			catch(NullPointerException e)
			{
				e.printStackTrace();
				// continueLoop = false;
			}

		}
		System.out.println("Stopping IOHandler");
		end();
	}

	/**
	 * Stops the IOHandler
	 */
	private void end()
	{

		// getOut().write("/disconnect");

		continueLoop = false;
		try
		{
			InStream.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		OutStream.close();
	}

	@Override
	public String getActorName()
	{

		return name;
	}

	@Override
	public void setActorName(String s)
	{

		name = s;
	}

	/**
	 * @return the OutputStream as a PrintWriter
	 */
	public PrintWriter getOut()
	{

		return OutStream;
	}

	@Override
	protected void finalize() throws Throwable
	{

		end();
		super.finalize();

	}

	/**
	 * @return if the end() method was called or the run method ended
	 */
	public boolean hasNotStopped()
	{

		return continueLoop;
	}

	@Override
	public void disconnect()
	{

		end();

	}

}
