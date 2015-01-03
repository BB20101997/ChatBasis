package bb.chat.security;

import bb.chat.interfaces.IIOHandler;
import bb.util.file.database.FileWriter;
import bb.util.file.database.ISaveAble;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 07.09.2014.
 */
public class BasicPermissionRegistrie implements ISaveAble {

	protected final List<String> registeredPermissions = new ArrayList<>();
	protected final List<Group>  registeredGroups      = new ArrayList<>();

	public void createPermission(String p) {

		synchronized(registeredPermissions) {
			if(!registeredPermissions.contains(p)) {
				registeredPermissions.add(p);
			}
		}
	}

	public void removePermission(String s) {

		synchronized(registeredPermissions) {
			if(registeredPermissions.contains(s)) {
				registeredPermissions.remove(s);
			}
		}
	}

	public void createGroup(String name) {
		synchronized(registeredGroups) {
			if(getGroup(name) == null) {
				Group g = new Group();
				g.name = name;
				registeredGroups.add(g);
			}
		}

	}

	public void deleteGroup(String name) {

		synchronized(registeredGroups) {
			Group g;
			if((g = getGroup(name)) != null) {
				registeredGroups.remove(g);
			}
		}
	}

	public void addGroupPermission(String nameGroup, String perm) {
		Group g;
		synchronized(registeredGroups) {
			if((g = getGroup(nameGroup)) != null) {
				if(!g.groupPerm.contains(perm)) {
					g.groupPerm.add(perm);
				}
			}
		}
	}

	public void removeGroupPermission(String nameGroup, String perm) {
		Group g;
		synchronized(registeredGroups) {
			if((g = getGroup(nameGroup)) != null && g.groupPerm.contains(perm)) {
				g.groupPerm.remove(perm);
			}
		}
	}

	public boolean hasPermission(BasicUser iup, List<String> permission) {
		List<String> groups = getGroups(iup);

		List<String> permissions = new ArrayList<>();
		permissions.addAll(getGroupsPermission(groups));
		permissions.addAll(iup.getUserPermission());

		List<String> deniedPermissions = new ArrayList<>();
		deniedPermissions.addAll(getGroupsDeniedPermission(groups));
		deniedPermissions.addAll(iup.getUserDeniedPermission());

		return hasPermission(permissions, deniedPermissions, permission);

	}

	private List<String> getGroupsDeniedPermission(List<String> groups) {
		List<String> perms = new ArrayList<>();
		Group g;
		for(String s : groups) {
			if((g = getGroup(s)) != null) {
				for(String perm : g.groupDeniedPerm) {
					if(!perms.contains(perm)) {
						perms.add(perm);
					}
				}
			}
		}

		return perms;
	}

	private Group getGroup(String name) {
		for(Group g : registeredGroups) {
			if(g.name.equals(name)) {
				return g;
			}
		}
		return null;
	}

	private List<String> getGroupsPermission(List<String> groups) {
		List<String> perms = new ArrayList<>();
		Group g;
		for(String s : groups) {
			if((g = getGroup(s)) != null) {
				for(String perm : g.groupPerm) {
					if(!perms.contains(perm)) {
						perms.add(perm);
					}
				}
			}
		}

		return perms;
	}

	private List<String> getGroups(BasicUser iup) {

		List<String> groups = new ArrayList<>();

		groups.addAll(iup.getGroups());

		for(String s : groups) {
			getSubGroups(s, groups);
		}

		return groups;
	}

	private void getSubGroups(String s, List<String> gr) {
		Group g = getGroup(s);
		if(g != null && !gr.contains(g.name)) {
			gr.add(g.name);
			getSubGroups(g.name, gr);
		}
	}

	public boolean hasPositivePermission(BasicUser iup, List<String> neededPermission) {
		List<String> groups = getGroups(iup);

		List<String> permissions = new ArrayList<>();
		permissions.addAll(getGroupsPermission(groups));
		permissions.addAll(iup.getUserPermission());

		return hasPermission(permissions, new ArrayList<String>(), neededPermission);

	}

	public boolean hasNegativePermission(BasicUser iup, List<String> neededPermission) {

		List<String> groups = getGroups(iup);

		List<String> deniedPermissions = new ArrayList<>();
		deniedPermissions.addAll(getGroupsDeniedPermission(groups));
		deniedPermissions.addAll(iup.getUserDeniedPermission());

		return !hasPermission(registeredPermissions, deniedPermissions, neededPermission);

	}

	boolean hasPermission(List<String> presentPermissions, List<String> deniedPermission, List<String> neededPermissions) {
		for(String p : neededPermissions) {
			if(!hasPermission(presentPermissions, deniedPermission, p)) {
				return false;
			}
		}
		return true;
	}


	@SuppressWarnings("unchecked")
	boolean hasPermission(List<String> presentPermissions, List<String> deniedPermission, String permission) {

		for(String p : deniedPermission) {
			if(includesPermission(p, permission)) {
				return false;
			}
		}

		for(String p : presentPermissions) {
			if(includesPermission(p, permission)) {
				return true;
			}
		}

		return false;
	}

	private boolean includesPermission(String deniedPermission, String permission) {
		//TODO:IMPLEMENT
		return false;
	}

	public void setPermission(IIOHandler sender, IIOHandler user, String command, String perm) {
		//TODO: Implement
	}

	public synchronized void writeToFileWriter(FileWriter fw) {
		fw.add(registeredPermissions.size(),"RPS");
		for(int i= 0;i<registeredPermissions.size();i++){
			fw.add(registeredPermissions.get(i),"PR"+i);
		}
		fw.add(registeredGroups.size(),"RGS");
		for(int i = 0;i<registeredGroups.size();i++){
			fw.add(registeredGroups.get(i),"RG"+i);
		}
	}

	public synchronized void loadFromFileWriter(FileWriter fw) {
		registeredPermissions.clear();
		registeredGroups.clear();

		int rps = (int) fw.get("PRS");
		int rgs = (int) fw.get("RGS");

		for(int i = 0;i<rps;i++){
			registeredPermissions.add((String) fw.get("PR"+i));
		}
		for(int i = 0;i<rgs;i++){
			Group g = new Group();
			g.loadFromFileWriter((FileWriter) fw.get("RG"+i));
			registeredGroups.add(g);
		}

	}

	private static class Group implements ISaveAble{

		public List<String> groupPerm;
		public List<String> groupDeniedPerm;
		public List<String> groups;
		public String       name;

		@Override
		public synchronized void writeToFileWriter(FileWriter fw) {
			fw.add(name,"NAME");
			fw.add(groupPerm.size(),"GPS");
			for(int i = 0;i<groupPerm.size();i++){
				fw.add(groupPerm.get(i),"GP"+i);
			}
			fw.add(groupDeniedPerm.size(),"GDPS");
			for(int i=0;i<groupDeniedPerm.size();i++){
				fw.add(groupDeniedPerm.get(i),"GDP"+i);
			}
			fw.add(groups.size(),"GS");
			for(int i = 0;i<groups.size();i++){
				fw.add(groups.get(i),"G"+i);
			}
		}

		@Override
		public synchronized void loadFromFileWriter(FileWriter fw) {
			groupPerm.clear();
			groupDeniedPerm.clear();
			groups.clear();

			name = (String) fw.get("NAME");
			int gps = (int) fw.get("GPS");
			int gdps = (int) fw.get("GDPS");
			int gs = (int) fw.get("GS");
			for(int i=0;i<gps;i++){
				groupPerm.add((String) fw.get("GP"+i));
			}
			for(int i=0;i<gdps;i++){
				groupDeniedPerm.add((String) fw.get("GDP"+i));
			}
			for(int i=0;i<gs;i++){
				groups.add((String) fw.get("G"+i));
			}
		}
	}

}
