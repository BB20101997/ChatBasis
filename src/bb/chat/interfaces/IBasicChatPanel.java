package bb.chat.interfaces;

public interface IBasicChatPanel {

	//wipe the log
	void WipeLog();

	//add the string to log
	void print(String s);

	//ad the string to log followed by a new line
	void println(String s);

}
