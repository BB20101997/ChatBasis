package bb.chat.interfaces;

import java.util.List;

/**
 * Created by BB20101997 on 07.09.2014.
 */
public interface IPermissionRegistrie<T, P extends IPermission<T>, G extends IUserPermissionGroup<T, P, G>> {

	void registerPermission(P s);

	void registerUserGroup(G group);

	void deleteUserGroup(G group);

	G getUserGroupByName(String name);

	boolean hasPermission(IUserPermission<T, P, G> iup, List<P> permission);

	boolean hasNegativePermission(IUserPermission<T, P, G> iup, List<P> permission);

	boolean hasPositivePermission(IUserPermission<T, P, G> iup, List<P> permission);

}
