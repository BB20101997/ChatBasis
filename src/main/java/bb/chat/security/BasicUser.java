package bb.chat.security;

import bb.util.file.database.FileWriter;
import bb.util.file.database.ISaveAble;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 06.12.2014.
 */
//assume permission check already passed when arriving here
public class BasicUser implements ISaveAble {

	private String name;
	private int    id;
	private String passwd;
	private final List<String> groups = new ArrayList<>();
	private final List<String> perm   = new ArrayList<>();
	private final List<String> denied = new ArrayList<>();


	public String getUserName() {
		return name;
	}


	public int getUserID() {
		return id;
	}


	public void setUserID(int i) {
		id = i;
	}


	@SuppressWarnings("BooleanMethodNameMustStartWithQuestion")
	public boolean changePassword(String old, String wen) {
		//TODO: add PermissionCheck not Possible no access to permission registrie!?
		if(passwd.equals(old)) {
			passwd = wen;
			return true;
		}
		return false;
	}

	public void setPassword(String s) {
		passwd = s;
	}

	public void changeUsername(String s) {
		name = s;
	}

	public List<String> getUserPermission() {
		return new ArrayList<>(perm);
	}


	public List<String> getUserDeniedPermission() {
		return new ArrayList<>(denied);
	}


	public void addUserPermission(String s) {
		synchronized(perm) {
			if(!perm.contains(s)) {
				perm.add(s);
			}
		}
	}


	public void removeUserPermission(String s) {
		synchronized(perm) {
			if(perm.contains(s)) {
				perm.remove(s);
			}
		}
	}


	public void addUserDeniedPermission(String s) {
		synchronized(denied) {
			if(!denied.contains(s)) {
				denied.add(s);
			}
		}
	}


	public void removeUserDeniedPermission(String s) {
		synchronized(denied) {
			if(denied.contains(s)) {
				denied.remove(s);
			}
		}
	}


	public List<String> getGroups() {
		return new ArrayList<>(groups);
	}


	public void addGroups(String s) {
		synchronized(groups) {
			if(!groups.contains(s)) {
				groups.add(s);
			}
		}
	}


	public void removeGroups(String s) {
		synchronized(groups) {
			if(groups.contains(s)) {
				groups.remove(s);
			}
		}
	}

	//TODO add hash
	public boolean checkPassword(String s) {
		return passwd.equals(s);
	}

	public void setUserName(String name) {
		this.name = name;
	}

	private final static String NAME_KEY = "NAME", ID_KEY = "ID", PASSWORD_KEY = "PASSWORD", GROUP_SIZE_KEY = "GS", PERMISSION_SIZE_KEY = "PS", DENIED_SIZE_KEY = "DS", GROUP_KEY = "G", PERMISSION_KEY = "P", DENIED_KEY = "D";

	@SuppressWarnings({"StringConcatenationMissingWhitespace", "StringConcatenation"})
	@Override
	public synchronized void writeToFileWriter(FileWriter fw) {
		fw.add(name, NAME_KEY);
		fw.add(id, ID_KEY);
		fw.add(passwd, PASSWORD_KEY);
		fw.add(groups.size(), GROUP_SIZE_KEY);
		fw.add(perm.size(), PERMISSION_SIZE_KEY);
		fw.add(denied.size(), DENIED_SIZE_KEY);
		for(int i = 0; i < groups.size(); i++) {
			fw.add(groups.get(i), GROUP_KEY + i);
		}
		for(int i = 0; i < perm.size(); i++) {
			fw.add(perm.get(i), PERMISSION_KEY + i);
		}
		for(int i = 0; i < denied.size(); i++) {
			fw.add(denied.get(i), DENIED_KEY + i);
		}
	}

	@SuppressWarnings({"StringConcatenationMissingWhitespace", "StringConcatenation"})
	@Override
	public synchronized void loadFromFileWriter(FileWriter fw) {
		groups.clear();
		perm.clear();
		denied.clear();

		name = (String) fw.get(NAME_KEY);
		id = (int) fw.get(ID_KEY);
		passwd = (String) fw.get(PASSWORD_KEY);
		int gs = (int) fw.get(GROUP_SIZE_KEY);
		int ds = (int) fw.get(DENIED_SIZE_KEY);
		int ps = (int) fw.get(PERMISSION_SIZE_KEY);
		for(int i = 0; i < gs; i++) {
			groups.add((String) fw.get(GROUP_KEY + i));
		}
		for(int i = 0; i < ps; i++) {
			perm.add((String) fw.get(PERMISSION_KEY + i));
		}
		for(int i = 0; i < ds; i++) {
			denied.add((String) fw.get(DENIED_KEY + i));
		}
	}
}
