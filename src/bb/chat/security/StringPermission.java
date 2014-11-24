package bb.chat.security;

import bb.chat.interfaces.IPermission;
import bb.chat.security.basics.BasicPermission;
import bb.util.file.database.FileWriter;

/**
 * Created by BB20101997 on 07.09.2014.
 */
public class StringPermission extends BasicPermission<String> {

	public StringPermission(){
	}

	public StringPermission(String permission) {
		super(permission);
	}

	@Override
	public boolean includesPermission(IPermission<String> permission) {
		//TODO:Implement functionality
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		return obj != null && obj instanceof StringPermission && ((StringPermission) obj).perm.equals(perm);
	}

	@Override
	public void writeToFileWriter(FileWriter fw) {
		fw.add(perm,"Permission");
	}

	@Override
	public void loadFromFileWriter(FileWriter fw) {
		perm = (String)fw.get("Permission");
	}
}
