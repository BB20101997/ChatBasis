package bb.chat.gui;

import bb.chat.interfaces.IBasicChatPanel;
import bb.chat.interfaces.IMessageHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author BB20101997
 */
public class BasicChatPanel extends JPanel implements ActionListener, IBasicChatPanel {
	/**
	 * the Command send by the Send button
	 */
	private static final String SEND_EVENT  = "BUTTON_SEND";
	/**
	 * the Command send when hitting Enter in the Text Field ,should be treated like SEND_EVENT
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

	public final IMessageHandler IMH;

	/**
	 * The constructor to set up the JPanel
	 */
	public BasicChatPanel(IMessageHandler imh) {
		super();
		IMH = imh;
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

	void setUseStandardActionListener(boolean b) {
		useStandardActionListener = b;
	}

	/**
	 * @param a adds an ActionListener to the Send Button and the ChatSendBar
	 */
	void addActionListener(ActionListener a) {

		Send.addActionListener(a);
		ChatSendBar.addActionListener(a);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if(useStandardActionListener) {
			String lastSend = ChatSendBar.getText();
			if(!"".equals(lastSend)){
				IMH.Message(lastSend);
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
	@Override
	public void WipeLog() {

		ChatLog.setText("");
	}

	/**
	 * @param s prints to the ChatLog
	 */
	@Override
	public void print(String s) {

		ChatLog.append(s);
	}

}
