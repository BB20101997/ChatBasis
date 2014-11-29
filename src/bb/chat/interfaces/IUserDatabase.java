package bb.chat.interfaces;

import bb.util.file.database.ISaveAble;

/**
 * Created by BB20101997 on 26.11.2014.
 */
public interface IUserDatabase<U extends IUser> extends ISaveAble {

	U getUserByID(int i);

	U getUserByName(String s);

	void addUserToDatabase(U u);

	void removeUserFromDatabase(U u);

}
