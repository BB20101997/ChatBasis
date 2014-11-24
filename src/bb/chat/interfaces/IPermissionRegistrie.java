package bb.chat.interfaces;

import bb.util.file.database.ISaveAble;

import java.util.List;

/**
 * Created by BB20101997 on 07.09.2014.
 */
public interface IPermissionRegistrie<P extends IPermission, G extends IUserPermissionGroup<P>> extends ISaveAble{

	void registerPermission(P s);

	void registerUserGroup(G group);

	void deleteUserGroup(G group);

	G getUserGroupByName(String name);

	boolean hasPermission(IUserPermission<P> iup, List<P> permission);

	boolean hasNegativePermission(IUserPermission<P> iup, List<P> permission);

	boolean hasPositivePermission(IUserPermission<P> iup, List<P> permission);

}
