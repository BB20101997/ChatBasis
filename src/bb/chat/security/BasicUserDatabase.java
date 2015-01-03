package bb.chat.security;

import bb.chat.interfaces.IMessageHandler;
import bb.util.file.database.FileWriter;
import bb.util.file.database.ISaveAble;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 06.12.2014.
 */
public class BasicUserDatabase implements ISaveAble {

	private final List<BasicUser> bul      = new ArrayList<>();
	private       int             nextFree = 0;

	public BasicUser getUserByID(int i) {
		for(BasicUser b : bul) {
			if(b.getUserID() == i) {
				return b;
			}
		}
		return null;
	}

	public BasicUser getUserByName(String s) {
		for(BasicUser b : bul) {
			if(b.getUserName().equals(s)) {
				return b;
			}
		}
		return null;
	}

	public boolean doesUserExist(String name) {
		for(BasicUser b : bul) {
			if(b.getUserName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public BasicUser createAndAddNewUser(String name,String passwd) {
		synchronized(bul) {
			if(!doesUserExist(name)) {
				BasicUser b = new BasicUser();
				b.setUserName(name);
				b.setPassword(passwd, IMessageHandler.SERVER.getUser());
				b.setUserID(nextFree);
				nextFree++;
				bul.add(b);
				return b;
			}
		}
		return null;
	}

	public void removeUserFromDatabase(BasicUser u) {
		synchronized(bul) {
			if(bul.contains(u)) {
				bul.remove(u);
			}
		}
	}

	public void removeUserFromDatabase(String name){
		synchronized(bul){
			if(doesUserExist(name)){
				bul.remove(getUserByName(name));
			}
		}

	}

	@Override
	public void writeToFileWriter(FileWriter fw) {

	}

	@Override
	public void loadFromFileWriter(FileWriter fw) {

	}
}
