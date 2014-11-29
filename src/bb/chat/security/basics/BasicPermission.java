package bb.chat.security.basics;

import bb.chat.interfaces.IPermission;

/**
 * Created by BB20101997 on 07.09.2014.
 */
public abstract class BasicPermission<T> implements IPermission<T> {

	protected BasicPermission() {

	}

	protected T perm;

	protected BasicPermission(T t) {
		perm = t;
	}

}
