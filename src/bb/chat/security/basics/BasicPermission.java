package bb.chat.security.basics;

import bb.chat.interfaces.IPermission;

/**
 * Created by BB20101997 on 07.09.2014.
 */
public abstract class BasicPermission<T> implements IPermission<T> {

	protected final T perm;

	public BasicPermission(T t) {
		perm = t;
	}

	@Override
	public T fullPermission() {
		return perm;
	}
}
