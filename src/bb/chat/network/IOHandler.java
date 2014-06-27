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
				}
			}
			catch(IOException e)
			{
				e.printStackTrace();
				continueLoop = false;
			}
			catch(NullPointerException e)
			{
				continueLoop = false;
			}

		}
		System.out.println("Stopping IOHandler");
		end();
	}

	/**
	 * Stops the IOHandler
	 */
	public void end()
	{

		continueLoop = false;
		try
		{
			InStream.close();
			OutStream.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
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

		super.finalize();
		end();
	}

	/**
	 * @return if the end() method was called or the run method ended
	 */
	public boolean hasNotStopped()
	{

		return continueLoop;
	}

}
