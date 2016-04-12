package bb.chat.security;

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
		for(BasicUser bu : bul) {
			if(bu.getUserID() == i) {
				return bu;
			}
		}
		return null;
	}

	public BasicUser getUserByName(String s) {
		for(BasicUser bu : bul) {
			if(bu.getUserName().equals(s)) {
				return bu;
			}
		}
		return null;
	}

	public synchronized boolean doesUserExist(String name) {
		for(BasicUser bu : bul) {
			if(bu.getUserName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public synchronized BasicUser createAndAddNewUser(String name, String passwd) {
		if(!doesUserExist(name)) {
			BasicUser bu = new BasicUser();
			bu.setUserName(name);
			bu.setPassword(passwd);
			bu.setUserID(nextFree);
			nextFree++;
			bul.add(bu);
			return bu;
		}

		return null;
	}

	public synchronized void removeUserFromDatabase(BasicUser u) {
		if(bul.contains(u)) {
			bul.remove(u);
		}

	}

	public synchronized void removeUserFromDatabase(String name) {
		if(doesUserExist(name)) {
			bul.remove(getUserByName(name));
		}

	}

	@SuppressWarnings("StringConcatenationMissingWhitespace")
	@Override
	public synchronized void writeToFileWriter(FileWriter fw) {
		fw.add(nextFree, "next");
		fw.add(bul.size(), "USIZE");
		for(int i = 0; i < bul.size(); i++) {
			fw.add(bul.get(i), "U" + i);
		}
	}

	@SuppressWarnings("StringConcatenationMissingWhitespace")
	@Override
	public synchronized void loadFromFileWriter(FileWriter fw) {
		nextFree = (int) fw.get("next");
		int size = (int) fw.get("USIZE");
		for(int i = 0; i < size; i++) {
			FileWriter fw2 = (FileWriter) fw.get("U" + i);
			BasicUser bu = new BasicUser();
			bu.loadFromFileWriter(fw2);
			bul.add(bu);

		}
	}
}
