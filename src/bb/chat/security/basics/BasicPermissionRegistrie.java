package bb.chat.security.basics;

import bb.chat.interfaces.IPermission;
import bb.chat.interfaces.IPermissionRegistrie;
import bb.chat.interfaces.IUserPermission;
import bb.chat.interfaces.IUserPermissionGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 07.09.2014.
 */
public abstract class BasicPermissionRegistrie<T, P extends IPermission<T>, G extends IUserPermissionGroup<T, P, G>> implements IPermissionRegistrie<T, P, G> {

	protected final List<P> registeredPermissions = new ArrayList<P>();
	protected final List<G> registeredUserGroups = new ArrayList<G>();

	@Override
	public void registerPermission(P p) {
		synchronized(registeredPermissions) {
			if(!registeredPermissions.contains(p)) {
				registeredPermissions.add(p);
			}
		}
	}

	@Override
	public void registerUserGroup(G group) {

		synchronized(registeredUserGroups) {
			for(G groups : registeredUserGroups) {
				if(groups.getGroupName().equals(group.getGroupName())) {
					return;
				}
			}

			registeredUserGroups.add(group);
		}
	}

	@Override
	public void deleteUserGroup(G group) {
		synchronized(registeredUserGroups) {
			if(registeredUserGroups.contains(group)) {
				registeredUserGroups.remove(group);
			}
		}
	}

	@Override
	public G getUserGroupByName(String name) {
		for(G group : registeredUserGroups) {
			if(group.getGroupName().equals(name)) {
				return group;
			}
		}
		return null;
	}

	protected List<String> getSubGroups(List<String> name) {

		List<String> s;

		for(String n : name) {
			s = getUserGroupByName(n).getContainedGroups();
			for(String b : s) {
				if(!name.contains(b)) {
					name.add(b);
					getSubGroups(name);
				}
			}
		}

		return name;
	}

	protected List<P> getGroupsPermission(List<String> names) {

		List<P> permissions = new ArrayList<P>();

		for(String name : names) {
			List<P> perms = getUserGroupByName(name).getPermissions();
			for(P perm : perms) {
				if(!permissions.contains(perm)) {
					permissions.add(perm);
				}
			}
		}

		return permissions;

	}

	protected List<P> getGroupsDeniedPermission(List<String> names) {

		List<P> permissions = new ArrayList<P>();

		for(String name : names) {
			List<P> perms = getUserGroupByName(name).getDeniedPermissions();
			for(P perm : perms) {
				if(!permissions.contains(perm)) {
					permissions.add(perm);
				}
			}
		}

		return permissions;

	}


	@Override
	public boolean hasPermission(IUserPermission<T, P, G> iup, List<P> permission) {
		List<String> groups = getSubGroups(iup.getContainedGroups());

		List<P> permissions = new ArrayList<P>();
		permissions.addAll(getGroupsPermission(groups));
		permissions.addAll(iup.getPermissions());

		List<P> deniedPermissions = new ArrayList<P>();
		deniedPermissions.addAll(getGroupsDeniedPermission(groups));
		deniedPermissions.addAll(iup.getDeniedPermissions());

		return hasPermission(permissions, deniedPermissions, permission);

	}


	@Override
	public boolean hasPositivePermission(IUserPermission<T, P, G> iup, List<P> permission) {

		List<String> groups = getSubGroups(iup.getContainedGroups());

		List<P> permissions = new ArrayList<P>();
		permissions.addAll(getGroupsPermission(groups));
		permissions.addAll(iup.getPermissions());

		return hasPermission(permissions, new ArrayList<P>(), permission);

	}

	@Override
	public boolean hasNegativePermission(IUserPermission<T, P, G> iup, List<P> permission) {

		List<String> groups = getSubGroups(iup.getContainedGroups());

		List<P> deniedPermissions = new ArrayList<P>();
		deniedPermissions.addAll(getGroupsDeniedPermission(groups));
		deniedPermissions.addAll(iup.getDeniedPermissions());

		return !hasPermission(registeredPermissions, deniedPermissions, permission);

	}

	protected boolean hasPermission(List<P> presentPermissions, List<P> deniedPermission, List<P> neededPermissions) {
		for(P p : neededPermissions) {
			if(!hasPermission(presentPermissions, deniedPermission, p)) {
				return false;
			}
		}
		return true;
	}

	protected boolean hasPermission(List<P> presentPermissions, List<P> deniedPermission, P permission) {

		for(P p : deniedPermission) {
			if(p.includesPermission(permission)) {
				return false;
			}
		}

		for(P p : presentPermissions) {
			if(p.includesPermission(permission)) {
				return true;
			}
		}

		return false;
	}
}
