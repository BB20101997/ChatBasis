package bb.chat.interfaces;

import bb.util.file.database.ISaveAble;

/**
 * Created by BB20101997 on 26.11.2014.
 */
public interface IUser<U extends IUserPermission> extends ISaveAble {

	String getUserName();

	int getUserID();

	boolean changePassword(String old, String wen, IUser<U> user);

	boolean setPassword(String s, IUser<U> user);

	boolean changeUsername(String s, IUser<U> user);

	U getUserPermission();

	void setUserPermission(U u, IUser<U> user);

	boolean checkPassword(String s);
}
