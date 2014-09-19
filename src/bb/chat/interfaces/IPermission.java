package bb.chat.interfaces;

/**
 * Created by BB20101997 on 07.09.2014.
 */
public interface IPermission<T> {
	T fullPermission();

	boolean includesPermission(IPermission<T> permission);
}
