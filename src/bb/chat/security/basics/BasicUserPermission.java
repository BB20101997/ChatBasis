package bb.chat.security.basics;

import bb.chat.interfaces.IPermission;
import bb.chat.interfaces.IUserPermission;
import bb.util.file.database.FileWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 07.09.2014.
 */
public class BasicUserPermission<P extends IPermission> implements IUserPermission<P> {

	protected final List<P>      permission       = new ArrayList<>();
	protected final List<P>      deniedPermission = new ArrayList<>();
	protected final List<String> groups           = new ArrayList<>();


	@Override
	public void addPermission(P perm) {
		synchronized(permission) {
			if(!permission.contains(perm)) {
				permission.add(perm);
			}
		}
	}

	@Override
	public void removePermission(P perm) {
		synchronized(permission) {
			if(permission.contains(perm)) {
				permission.remove(perm);
			}
		}
	}

	@Override
	public void addDeniedPermission(P perm) {
		synchronized(deniedPermission) {
			if(!deniedPermission.contains(perm)) {
				deniedPermission.add(perm);
			}
		}
	}

	@Override
	public void removeDeniedPermission(P perm) {
		synchronized(deniedPermission) {
			if(deniedPermission.contains(perm)) {
				deniedPermission.remove(perm);
			}
		}
	}

	@Override
	public List<String> getContainedGroups() {
		return groups;
	}

	@Override
	public void addUserPermissionGroup(String name) {
		synchronized(groups) {
			if(!groups.contains(name)) {
				groups.add(name);
			}
		}
	}

	@Override
	public void removeUserPermissionGroup(String name) {
		synchronized(groups) {
			if(groups.contains(name)) {
				groups.remove(name);
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<P> getPermissions() {
		return permission;

	}

	@Override
	@SuppressWarnings("unchecked")
	public List<P> getDeniedPermissions() {

		return deniedPermission;
	}

	@Override
	public void writeToFileWriter(FileWriter fw) {
		fw.add(groups.size(),"GROUPS-SIZE");
		fw.add(permission.size(),"PERMISSION-SIZE");
		fw.add(deniedPermission.size(),"DENIEPERMISSION-SIZE");
		for(int i = 0;i<groups.size();i++){
			fw.add(groups.get(i),"GROUPS"+i);
		}
		FileWriter fwPerm;
		for(int i = 0;i<permission.size();i++){
			fwPerm = new FileWriter();
			permission.get(i).writeToFileWriter(fwPerm);
			fw.add(fwPerm,"PERMISSION"+i);
			fw.add(permission.getClass().toString(),"PERMISSION-CLASS"+i);
		}
		for(int i = 0;i<deniedPermission.size();i++){
			fwPerm = new FileWriter();
			deniedPermission.get(i).writeToFileWriter(fwPerm);
			fw.add(fwPerm,"DENIEDPERMISSION"+i);
			fw.add(deniedPermission.getClass().toString(),"DENIEDPERMISSION-CLASS"+i);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void loadFromFileWriter(FileWriter fw) {
		int gs = (int) fw.get("GROUPS-SIZE");
		int ps = (int) fw.get("PERMISSION-SIZE");
		int dps = (int) fw.get("DENIEPERMISSION-SIZE");

		for(int i = 0;i<gs;i++){
			addUserPermissionGroup((String)fw.get("GROUPS"+i));
		}
		for(int i = 0;i<ps;i++){
			try {
				Class<P> c = (Class<P>) Class.forName((String) fw.get("PERMISSION-CLASS" + i));
				P  ip = c.newInstance();
				ip.loadFromFileWriter((FileWriter) fw.get("PERMISSION"+i));
				addPermission(ip);
			} catch(ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		for(int i = 0;i<ps;i++){
			try {
				Class<P> c = (Class<P>) Class.forName((String) fw.get("DENIEDPERMISSION-CLASS"+i));
				P  ip = c.newInstance();
				ip.loadFromFileWriter((FileWriter) fw.get("DENIEDPERMISSION"+i));
				addDeniedPermission(ip);
			} catch(ClassNotFoundException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
}
