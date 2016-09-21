package bb.chat.security;


import bb.chat.basis.BasisConstants;
import bb.util.file.database.FileWriter;
import bb.util.file.database.ISaveAble;
import bb.util.file.log.BBLogHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
/**
 * Created by BB20101997 on 06.12.2014.
 */
public class BasicUserDatabase implements ISaveAble {

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(BasicUserDatabase.class.getName());
		log.addHandler(new BBLogHandler(bb.util.file.log.Constants.getLogFile(BasisConstants.LOG_NAME)));
	}

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

	private static final String NEXT_KEY = "next",BUL_SIZE_KEY = "USIZE", USER_KEY = "U";

	@SuppressWarnings("StringConcatenationMissingWhitespace")
	@Override
	public synchronized void writeToFileWriter(FileWriter fileWriter) {
		fileWriter.add(nextFree,NEXT_KEY);
		fileWriter.add(bul.size(), BUL_SIZE_KEY);
		for(int i = 0; i < bul.size(); i++) {
			//noinspection StringConcatenation
			fileWriter.add(bul.get(i), USER_KEY + i);
		}
	}

	@SuppressWarnings("StringConcatenationMissingWhitespace")
	@Override
	public synchronized void loadFromFileWriter(FileWriter fileWriter) {
		nextFree = (int) fileWriter.get(NEXT_KEY);
		int size = (int) fileWriter.get(BUL_SIZE_KEY);
		for(int i = 0; i < size; i++) {
			//noinspection StringConcatenation
			FileWriter fw2 = (FileWriter) fileWriter.get(USER_KEY + i);
			BasicUser bu = new BasicUser();
			bu.loadFromFileWriter(fw2);
			bul.add(bu);

		}
	}
}
