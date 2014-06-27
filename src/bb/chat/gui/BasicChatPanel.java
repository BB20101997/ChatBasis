package bb.chat.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import bb.chat.interfaces.IMessageHandler;

/**
 * @author BB20101997
 */
public class BasicChatPanel extends JPanel implements ActionListener
{

	private static final long		serialVersionUID			= 1L;

	/**
	 * the Command send by the Send button
	 */
	public static final String		SENDEVENT					= "BUTTON_SEND";
	/**
	 * the Command send when hitting Enter in the Text Field ,sould be treated
	 * like SENDEVENT
	 */
	public static final String		ENTEREVENT					= "CSB_ENTER";

	/**
	 * if the Standard ActionListener Should be used
	 */
	public boolean					useStandartActionListener	= true;
	/**
	 * The Button to send the Text of the ChatSendBar
	 */
	public JButton					Send						= new JButton("Send");
	/**
	 * Latest Send Message
	 */
	public String					lastSend					= null;
	/**
	 * The ChatLog --Obvious--
	 */
	public JTextArea				ChatLog						= new JTextArea();
	/**
	 * The Field to enter a Message
	 */
	public JTextField				ChatSendBar					= new JTextField();
	private JScrollPane				ChatLogScroll				= new JScrollPane(ChatLog);
	private Box						boxBar						= Box.createHorizontalBox();
	private List<IMessageHandler>	MHList						= new ArrayList<IMessageHandler>();

	/**
	 * The constructer to set up the JPanel
	 */
	public BasicChatPanel()
	{

		super();
		Send.addActionListener(this);
		ChatSendBar.addActionListener(this);
		ChatLog.setEditable(false);
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, ChatLogScroll);
		boxBar.add(ChatSendBar);
		boxBar.add(Send);
		add(BorderLayout.SOUTH, boxBar);
		invalidate();
	}

	/**
	 * @param a
	 *            adds an ActionListener to the Send Button and the ChatSendBar
	 */
	public void addActionListener(ActionListener a)
	{

		Send.addActionListener(a);
		ChatSendBar.addActionListener(a);
	}

	@Override
	public void actionPerformed(ActionEvent arg0)
	{

		if(useStandartActionListener)
		{
			lastSend = ChatSendBar.getText();
			for(IMessageHandler MH : MHList)
			{
				MH.Message(lastSend, MH.getActor());
			}
			ChatSendBar.setText("");
			setSize(getSize());
			invalidate();

		}

	}

	/**
	 * @param M
	 *            added to the MessageHandler List
	 */
	public void addMessageHandler(IMessageHandler M)
	{

		MHList.add(M);
	}

	/**
	 * Clears the ChatLog
	 */
	public void WipeLog()
	{

		ChatLog.setText("");
	}

	/**
	 * @param s
	 *            prints to the ChatLog
	 */
	public void print(String s)
	{

		ChatLog.append(s);
	}

	/**
	 * @param s
	 *            prints to the ChatLog and adds a NewLine at the end
	 */
	public void println(String s)
	{

		ChatLog.append(s + "\n");
	}

}
