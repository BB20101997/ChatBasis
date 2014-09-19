package bb.chat.security;

import bb.chat.interfaces.IPermission;
import bb.chat.security.basics.BasicPermission;

/**
 * Created by BB20101997 on 07.09.2014.
 */
public class StringPermission extends BasicPermission<String> {

	public StringPermission(String perm) {
		super(perm);
	}

	@Override
	public boolean includesPermission(IPermission<String> permission) {
		//TODO:Implement functionality
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof StringPermission && ((StringPermission) obj).fullPermission().equals(fullPermission());
	}
}
