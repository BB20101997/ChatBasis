package bb.chat.security.string;

import bb.chat.security.basics.BasicPermissionRegistrie;
import bb.util.file.database.FileWriter;

/**
 * Created by BB20101997 on 07.09.2014.
 */
public class StringPermissionRegistrie extends BasicPermissionRegistrie<StringPermission, StringUserPermissionGroup> {

	private static final String keyRegPerm      = "RegPerm";
	private static final String keyRegGroupPerm = "RegGroupPerm";

	@Override
	public void writeToFileWriter(FileWriter fw) {
		fw.add(registeredPermissions.size(), keyRegPerm);
		fw.add(registeredUserGroups.size(), keyRegGroupPerm);
		FileWriter fwPerm;
		for(int i = 0; i < registeredPermissions.size(); i++) {
			fwPerm = new FileWriter();
			registeredPermissions.get(i).writeToFileWriter(fwPerm);
			fw.add(fwPerm, keyRegPerm + i);
		}
		for(int i = 0; i < registeredUserGroups.size(); i++) {
			fwPerm = new FileWriter();
			registeredUserGroups.get(i).writeToFileWriter(fwPerm);
			fw.add(fwPerm, keyRegGroupPerm + i);
		}

	}

	@Override
	public void loadFromFileWriter(FileWriter fw) {
		int permSize = (int) fw.get(keyRegPerm);
		int permGroupSize = (int) fw.get(keyRegGroupPerm);
		StringPermission sp;
		for(int i = 0; i < permSize; i++) {
			sp = new StringPermission();
			sp.loadFromFileWriter((FileWriter) fw.get(keyRegPerm + i));
			registerPermission(sp);
		}
		StringUserPermissionGroup sup;
		for(int i = 0; i < permGroupSize; i++) {
			sup = new StringUserPermissionGroup();
			sup.loadFromFileWriter((FileWriter) fw.get(keyRegGroupPerm + i));
			registerUserGroup(sup);
		}

	}
}
