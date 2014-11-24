package bb.chat.interfaces;

import bb.util.file.database.ISaveAble;

/**
 * Created by BB20101997 on 07.09.2014.
 */
public interface IPermission<T> extends ISaveAble{

	@SuppressWarnings("UnusedParameters")
	boolean includesPermission(IPermission<T> permission);

}
