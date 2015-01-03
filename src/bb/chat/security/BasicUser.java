package bb.chat.security;

import bb.util.file.database.FileWriter;
import bb.util.file.database.ISaveAble;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 06.12.2014.
 */
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


	public boolean changePassword(String old, String wen, BasicUser user) {
		//TODO: add PermissionCheck
		if(passwd.equals(old)) {
			passwd = wen;
		}
		return false;
	}


	public boolean setPassword(String s, BasicUser user) {
		//TODO: add PermissionCheck
		passwd = s;
		return false;
	}


	public boolean changeUsername(String s, BasicUser user) {
		//TODO: add PermissionCheck
		name = s;
		return false;
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


	public boolean checkPassword(String s) {
		return passwd.equals(s);
	}

	public void setUserName(String name) {
		this.name = name;
	}

	@Override
	public synchronized void writeToFileWriter(FileWriter fw) {
		fw.add(name,"NAME");
		fw.add(id,"ID");
		fw.add(passwd,"PASSWORD");
		fw.add(groups.size(),"GS");
		fw.add(perm.size(),"PS");
		fw.add(denied.size(),"DS");
		for(int i = 0;i<groups.size();i++){
			fw.add(groups.get(i),"G"+i);
		}
		for(int i = 0;i<perm.size();i++){
			fw.add(perm.get(i),"P"+i);
		}
		for(int i = 0;i<denied.size();i++){
			fw.add(denied.get(i),"D"+i);
		}
	}

	@Override
	public synchronized void loadFromFileWriter(FileWriter fw) {
		groups.clear();
		perm.clear();
		denied.clear();

		name = (String) fw.get("NAME");
		id = (int) fw.get("ID");
		passwd = (String) fw.get("PASSWORD");
		int gs = (int) fw.get("GS");
		int ds = (int) fw.get("DS");
		int ps = (int) fw.get("PS");
		for(int i = 0;i<gs;i++){
			groups.add((String) fw.get("G"+i));
		}
		for(int i = 0;i<ps;i++){
			perm.add((String) fw.get("P"+i));
		}
		for(int i = 0;i<ds;i++){
			denied.add((String) fw.get("D"+i));
		}
	}
}
