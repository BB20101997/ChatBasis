package bb.chat.interfaces;

import bb.util.file.database.ISaveAble;

/**
 * Created by BB20101997 on 07.09.2014.
 */
public interface IUserPermissionGroup<P extends IPermission> extends IUserPermission<P>,ISaveAble{

	String getGroupName();

	void setGroupName(String name);
}
