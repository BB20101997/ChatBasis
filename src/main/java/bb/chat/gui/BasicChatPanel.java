package bb.chat.gui;

import bb.chat.enums.Bundles;
import bb.chat.interfaces.IBasicChatPanel;
import bb.chat.interfaces.IChat;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.MessageFormat;
import java.util.logging.Logger;

import static bb.chat.basis.BasisConstants.LOG_NAME;

/**
 * @author BB20101997
 */
public class BasicChatPanel extends JPanel implements ActionListener, KeyListener, IBasicChatPanel {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(BasicChatPanel.class.getName());
		//noinspection DuplicateStringLiteralInspection
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}

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
	private       boolean    useStandardKeyListener = true;
	/**
	 * The Button to send the Text of the ChatSendBar
	 */
	private final JButton    Send                      = new JButton();
	/**
	 * The ChatLog --Obvious--
	 */
	private final JTextArea  ChatLog                   = new JTextArea();
	/**
	 * The Field to enter a Message
	 */
	private final JTextField ChatSendBar               = new JTextField();

	private final JScrollPane chatLogScroll = new JScrollPane(ChatLog);

	private final IChat iChat;

	private volatile boolean stayBottom = true;

	/**
	 * The constructor to set up the JPanel
	 */
	@SuppressWarnings("unused")
	public BasicChatPanel(IChat ic) {
		super();
		iChat = ic;
		Send.setActionCommand(SEND_EVENT);
		ChatSendBar.setActionCommand(ENTER_EVENT);
		ChatSendBar.addKeyListener(this);
		ChatSendBar.setFocusTraversalKeysEnabled(false);
		addActionListener(this);
		ChatLog.setEditable(false);
		setLayout(new BorderLayout());
		add(BorderLayout.CENTER, chatLogScroll);
		Box boxBar = Box.createHorizontalBox();
		boxBar.add(ChatSendBar);
		setButtonLabel();
		boxBar.add(Send);
		add(BorderLayout.SOUTH, boxBar);
		invalidate();

		setUseStandardActionListener(true);
		setUseStandardKeyListener(true);

	}

	private void setButtonLabel(){
		Send.setText(Bundles.BUTTON_LABEL.getResource().getString("button.send"));
	}

	/**
	 * can be used to disable the default processing of input
	 */
	//sets if the Standard actionListener should be used
	@SuppressWarnings("SameParameterValue")
	void setUseStandardActionListener(boolean use) {
		this.useStandardActionListener = use;
	}

	@SuppressWarnings("SameParameterValue")
	public void setUseStandardKeyListener(boolean use) {
		this.useStandardKeyListener = use;
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
	public void actionPerformed(ActionEvent e) {
	log.fine(MessageFormat.format(Bundles.LOG_TEXT.getResource().getString("log.chat.action"),e));
		if(useStandardActionListener) {
			String lastSend = ChatSendBar.getText();
			if(lastSend != null && !lastSend.isEmpty()) {
				iChat.message(lastSend);
			}
			ChatSendBar.setText("");
			setSize(getSize());
			invalidate();
		}
	}

	private void autoBottom(){
		if(stayBottom) {
			chatLogScroll.getVerticalScrollBar().setValue(chatLogScroll.getVerticalScrollBar().getMaximum());
		}
	}

	public void setStayBottom(boolean stayBottom) {
		this.stayBottom = stayBottom;
	}

	public boolean isStayBottom(){
		return stayBottom;
	}

	/**
	 * Clears the ChatLog
	 */
	//wipes the logs displayed
	@Override
	public void wipeLog() {
		log.fine(Bundles.LOG_TEXT.getResource().getString("log.chat.wipe"));
		ChatLog.setText("");
	}

	/**
	 * @param s prints to the ChatLog
	 */
	//adds a string to the log displayed
	@Override
	public void print(String s) {
		log.fine(MessageFormat.format(Bundles.LOG_TEXT.getResource().getString("log.chat.append"),s));
		ChatLog.append(s);
		autoBottom();
	}
	//adds a string to the log followed by a new line
	@SuppressWarnings("StringConcatenationMissingWhitespace")
	public void println(String s) {
		log.fine(MessageFormat.format(Bundles.LOG_TEXT.getResource().getString("log.chat.new_line"),s));
		//noinspection StringConcatenation
		print(s+System.lineSeparator());
	}

	@Override
	public void stop() {
		//noinspection DuplicateStringLiteralInspection
		log.fine(Bundles.LOG_TEXT.getResource().getString("log.shutdown.init"));
		println(Bundles.MESSAGE.getResource().getString("shutdown.init"));
	}

	private int tabCount = 0;

	@Override
	public void keyTyped(KeyEvent e) {
		log.fine(MessageFormat.format(Bundles.LOG_TEXT.getString("log.key.typed"),e));
		if(useStandardKeyListener){
			if(e.getKeyChar()=='\t'){
				//increase tab count by one
				tabCount++;
				//save the caret position
				int cp = ChatSendBar.getCaretPosition();
				//get the auto-completed text
				String s = iChat.getCommandRegistry().complete(ChatSendBar.getText(),cp,tabCount);
				if(s!=null) {
					//set the auto-completed text
					ChatSendBar.setText(s);
				}
				//restore caret position
				ChatSendBar.setCaretPosition(cp);
				e.consume();
			}
		}else{
			tabCount = 0;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		log.fine(MessageFormat.format(Bundles.LOG_TEXT.getString("log.key.pressed"), e));
		if(useStandardKeyListener) {
			//if the right arrow key is pressed
			if(e.getKeyCode()==KeyEvent.VK_RIGHT){
				//and there have been tabs
				if(tabCount > 0) {
					//move the caret to the and of the line
					ChatSendBar.setCaretPosition(ChatSendBar.getText().length());
					//and consume the event so it won't be processed twice
					e.consume();
				}
			}

			//reset tab counter if a key but tab is pressed
			if(e.getKeyCode()!=KeyEvent.VK_TAB){
				tabCount = 0;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		log.fine(MessageFormat.format(Bundles.LOG_TEXT.getString("log.key.released"), e));
		//if(useStandardKeyListener) {
		// atm not used
		//}
	}


}
