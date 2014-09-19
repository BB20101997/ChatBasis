package bb.chat.interfaces;

import java.util.List;

/**
 * Created by BB20101997 on 07.09.2014.
 */
public interface IUserPermission<T, P extends IPermission<T>, G extends IUserPermissionGroup<T, P, G>> {

	void addPermission(P perm);

	void removePermission(P perm);

	void addDeniedPermission(P perm);

	void removeDeniedPermission(P perm);

	List<String> getContainedGroups();

	/**
	 * It should not be allowed for a UserGroup to contain itself
	 */
	void addUserPermissionGroup(String name);

	void removeUserPermissionGroup(String name);

	List<P> getPermissions();

	List<P> getDeniedPermissions();

}
