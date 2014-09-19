package bb.chat.interfaces;

/**
 * Created by BB20101997 on 07.09.2014.
 */
public interface IUserPermissionGroup<T, P extends IPermission<T>, G extends IUserPermissionGroup<T, P, G>> extends IUserPermission<T, P, G> {

	String getGroupName();

	void setGroupName(String name);
}
