package bb.chat.gui;

import bb.chat.interfaces.IBasicChatPanel;
import bb.chat.interfaces.IMessageHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author BB20101997
 */
public class BasicChatPanel extends JPanel implements ActionListener, IBasicChatPanel
{
	/**
	 * the Command send by the Send button
	 */
	public static final String SEND_EVENT = "BUTTON_SEND";
	/**
	 * the Command send when hitting Enter in the Text Field ,should be treated
	 * like SEND_EVENT
	 */
	public static final String ENTER_EVENT = "CSB_ENTER";

	/**
	 * if the Standard ActionListener Should be used
	 */
    private boolean useStandardActionListener = true;
	/**
	 * The Button to send the Text of the ChatSendBar
	 */
    private final JButton					Send						= new JButton("Send");
    /**
	 * The ChatLog --Obvious--
	 */
    private final JTextArea				ChatLog						= new JTextArea();
	/**
	 * The Field to enter a Message
	 */
	public final JTextField				ChatSendBar					= new JTextField();
    private final List<IMessageHandler>	MHList						= new ArrayList<IMessageHandler>();

	/**
	 * The constructor to set up the JPanel
	 */
	public BasicChatPanel()
	{

		super();
        Send.setActionCommand(SEND_EVENT);
        ChatSendBar.setActionCommand(ENTER_EVENT);
        addActionListener(this);
		ChatLog.setEditable(false);
		setLayout(new BorderLayout());
        JScrollPane chatLogScroll = new JScrollPane(ChatLog);
        add(BorderLayout.CENTER, chatLogScroll);
        Box boxBar = Box.createHorizontalBox();
        boxBar.add(ChatSendBar);
		boxBar.add(Send);
		add(BorderLayout.SOUTH, boxBar);
		invalidate();
        setUseStandardActionListener(true);
	}

    /**
    * can be used to disable the default processing of input
    * */

     public void setUseStandardActionListener(boolean b){
        useStandardActionListener = b;
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

		if(useStandardActionListener)
		{
			/*
	  Latest Send Message
	 */
            String lastSend = ChatSendBar.getText();
			for(IMessageHandler MH : MHList)
			{
				MH.Message(lastSend);
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
	@Override
	public void addMessageHandler(IMessageHandler M)
	{

		MHList.add(M);
	}

	/**
	 * Clears the ChatLog
	 */
	@Override
	public void WipeLog()
	{

		ChatLog.setText("");
	}

	/**
	 * @param s
	 *            prints to the ChatLog
	 */
	@Override
	public void print(String s)
	{

		ChatLog.append(s);
	}

	/**
	 * @param s
	 *            prints to the ChatLog and adds a NewLine at the end
	 */
	@Override
	public void println(String s)
	{

		ChatLog.append(s + "\n");
	}

}
