package bb.chat.security.basics;

import bb.chat.interfaces.IPermission;
import bb.chat.interfaces.IUserPermissionGroup;

/**
 * Created by BB20101997 on 07.09.2014.
 */
public class BasicUserPermissionGroup<T, P extends IPermission<T>, G extends IUserPermissionGroup<T, P, G>> extends BasicUserPermission<T, P, G> implements IUserPermissionGroup<T, P, G> {

	protected String name = "";

	@Override
	public String getGroupName() {
		return name;
	}

	@Override
	public void setGroupName(String n) {
		name = n;
	}
}
