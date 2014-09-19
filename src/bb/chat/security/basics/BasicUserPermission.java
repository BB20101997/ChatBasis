package bb.chat.security.basics;

import bb.chat.interfaces.IPermission;
import bb.chat.interfaces.IUserPermission;
import bb.chat.interfaces.IUserPermissionGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 07.09.2014.
 */
public class BasicUserPermission<T, P extends IPermission<T>, G extends IUserPermissionGroup<T, P, G>> implements IUserPermission<T, P, G> {

	protected final List<P> permission = new ArrayList<P>();
	protected final List<P> deniedPermission = new ArrayList<P>();
	protected final List<String> groups = new ArrayList<String>();


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
}
