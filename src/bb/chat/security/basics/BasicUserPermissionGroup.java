package bb.chat.security.basics;

import bb.chat.interfaces.IPermission;
import bb.chat.interfaces.IUserPermissionGroup;
import bb.util.file.database.FileWriter;

/**
 * Created by BB20101997 on 07.09.2014.
 */
public class BasicUserPermissionGroup<P extends IPermission> extends BasicUserPermission<P> implements IUserPermissionGroup<P> {

	protected String name = "";

	@Override
	public String getGroupName() {
		return name;
	}


	@Override
	public void setGroupName(String n) {
		name = n;
	}

	@Override
	public void writeToFileWriter(FileWriter fw) {
		super.writeToFileWriter(fw);
		fw.add(name,"NAME");
	}

	@Override
	public void loadFromFileWriter(FileWriter fw) {
		super.loadFromFileWriter(fw);
		name = (String) fw.get("NAME");
	}
}
