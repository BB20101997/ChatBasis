package bb.chat.gui;

import bb.chat.interfaces.IBasicChatPanel;
import bb.chat.interfaces.IChat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author BB20101997
 */
public class BasicChatPanel extends JPanel implements ActionListener, IBasicChatPanel {
	/**
	 * the command send by the Send button
	 */
	private static final String SEND_EVENT  = "BUTTON_SEND";
	/**
	 * the command send when hitting Enter in the Text Field ,should be treated like SEND_EVENT
	 */
	private static final String ENTER_EVENT = "CSB_ENTER";

	/**
	 * if the Standard ActionListener Should be used
	 */
	private       boolean    useStandardActionListener = true;
	/**
	 * The Button to send the Text of the ChatSendBar
	 */
	private final JButton    Send                      = new JButton("Send");
	/**
	 * The ChatLog --Obvious--
	 */
	private final JTextArea  ChatLog                   = new JTextArea();
	/**
	 * The Field to enter a Message
	 */
	private final JTextField ChatSendBar               = new JTextField();

	private final JScrollPane chatLogScroll = new JScrollPane(ChatLog);

	public final IChat iChat;

	/**
	 * The constructor to set up the JPanel
	 */
	public BasicChatPanel(IChat ic) {
		super();
		iChat = ic;
		Send.setActionCommand(SEND_EVENT);
		ChatSendBar.setActionCommand(ENTER_EVENT);
		addActionListener(this);
		ChatLog.setEditable(false);
		setLayout(new BorderLayout());
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
	 */
	//sets if the Standard actionListener should be used
	void setUseStandardActionListener(@SuppressWarnings("SameParameterValue") boolean useStandardActionListener) {
		this.useStandardActionListener = useStandardActionListener;
	}

	/**
	 * @param a adds an ActionListener to the Send Button and the ChatSendBar
	 */
	//adds an actionListener
	void addActionListener(ActionListener a) {

		Send.addActionListener(a);
		ChatSendBar.addActionListener(a);
	}

	@Override
	//the standard actionListener
	public void actionPerformed(ActionEvent arg0) {

		if(useStandardActionListener) {
			String lastSend = ChatSendBar.getText();
			if(!"".equals(lastSend)) {
				iChat.Message(lastSend);
			}
			ChatSendBar.setText("");
			setSize(getSize());
			chatLogScroll.getVerticalScrollBar().setValue(ChatLog.getRows());
			invalidate();
		}

	}

	/**
	 * Clears the ChatLog
	 */
	//wipes the logs displayed
	@Override
	public void WipeLog() {

		ChatLog.setText("");
	}

	/**
	 * @param s prints to the ChatLog
	 */
	//adds a string to the log displayed
	@Override
	public void print(String s) {
		ChatLog.append(s);
	}
	//adds a string to the log followed by a new line
	public void println(String s) {
		print(s +System.lineSeparator());
	}

}
