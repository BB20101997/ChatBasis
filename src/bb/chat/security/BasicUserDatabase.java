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

	public BasicUser createAndAddNewUser(String name, String passwd) {
		synchronized(bul) {
			if(!doesUserExist(name)) {
				BasicUser b = new BasicUser();
				b.setUserName(name);
				b.setPassword(passwd, null);
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

	public void removeUserFromDatabase(String name) {
		synchronized(bul) {
			if(doesUserExist(name)) {
				bul.remove(getUserByName(name));
			}
		}

	}

	@SuppressWarnings("StringConcatenationMissingWhitespace")
	@Override
	public void writeToFileWriter(FileWriter fw) {
		fw.add(nextFree, "next");
		fw.add(bul.size(), "USIZE");
		for(int i = 0; i < bul.size(); i++) {
			fw.add(bul.get(i), "U" + i);
		}
	}

	@SuppressWarnings("StringConcatenationMissingWhitespace")
	@Override
	public void loadFromFileWriter(FileWriter fw) {
		nextFree = (int) fw.get("next");
		int size = (int) fw.get("USIZE");
		for(int i = 0; i < size; i++) {
			FileWriter f = (FileWriter) fw.get("U" + i);
			BasicUser b = new BasicUser();
			b.loadFromFileWriter(f);
			bul.add(b);

		}
	}
}
