package bb.chat.security;

import bb.chat.command.Permission;
import bb.chat.command.subcommands.permission.SubPermission;
import bb.chat.interfaces.IChat;
import bb.chat.interfaces.ICommand;
import bb.net.interfaces.IIOHandler;
import bb.util.file.database.FileWriter;
import bb.util.file.database.ISaveAble;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static bb.chat.base.Constants.LOG_NAME;

/**
 * Created by BB20101997 on 07.09.2014.
 */
@SuppressWarnings("unused")
public class BasicPermissionRegistrie implements ISaveAble {
//TODO clean up - editing groups can be done by retrieving group and editing the group
	private final List<String> registeredPermissions = new ArrayList<>();
	private final List<Group>  registeredGroups      = new ArrayList<>();

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(BasicPermissionRegistrie.class.getName());
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}

	public String[] getPermissionsRegistered(){
		return registeredPermissions.toArray(new String[registeredPermissions.size()]);
	}

	public boolean isPermissionRegistered(String p) {
		for(String s : registeredPermissions) {
			if(s.equals(p)) {
				return true;
			}
		}
		return false;
	}

	public void createPermission(String p) {

		synchronized(registeredPermissions) {
			if(!registeredPermissions.contains(p)) {
				registeredPermissions.add(p);
			}
		}
	}

	public void removePermission(String s) {

		synchronized(registeredPermissions) {
			if(registeredPermissions.contains(s)) {
				registeredPermissions.remove(s);
			}
		}
	}

	public void createGroup(String name) {
		synchronized(registeredGroups) {
			if(getGroup(name) == null) {
				Group g = new Group();
				g.name = name;
				registeredGroups.add(g);
			}
		}

	}

	public void deleteGroup(String name) {

		synchronized(registeredGroups) {
			Group g;
			if((g = getGroup(name)) != null) {
				registeredGroups.remove(g);
			}
		}
	}

	public void addGroupPermission(String nameGroup, String perm) {
		Group g;
			if((g = getGroup(nameGroup)) != null) {
				if(!g.groupPerm.contains(perm)) {
					g.groupPerm.add(perm);
				}
		}
	}

	public void removeGroupPermission(String nameGroup, String perm) {
		Group g;
		synchronized(registeredGroups) {
			if((g = getGroup(nameGroup)) != null && g.groupPerm.contains(perm)) {
				g.groupPerm.remove(perm);
			}
		}
	}

	private List<String> getGroupsDeniedPermission(List<String> groups) {
		List<String> perms = new ArrayList<>();
		Group g;
		for(String s : groups) {
			if((g = getGroup(s)) != null) {
				g.groupDeniedPerm.stream().filter(perm -> !perms.contains(perm)).forEach(perms::add);
			}
		}

		return perms;
	}

	private List<String> getGroupsPermission(List<String> groups) {
		List<String> perms = new ArrayList<>();
		Group g;
		for(String s : groups) {
			if((g = getGroup(s)) != null) {
				g.groupPerm.stream().filter(perm -> !perms.contains(perm)).forEach(perms::add);
			}
		}

		return perms;
	}

	private Group getGroup(String name) {
		for(Group g : registeredGroups) {
			if(g.name.equals(name)) {
				return g;
			}
		}
		return null;
	}

	private List<String> getGroups(BasicUser iup) {

		List<String> groups = new ArrayList<>();

		groups.addAll(iup.getGroups());

		for(String s : groups) {
			getSubGroups(s, groups);
		}

		return groups;
	}

	private void getSubGroups(String s, List<String> gr) {
		Group g = getGroup(s);
		if(g != null && !gr.contains(g.name)) {
			gr.add(g.name);
			getSubGroups(g.name, gr);
		}
	}

	public boolean hasPermission(BasicUser iup, List<String> permission) {
		List<String> groups = getGroups(iup);

		List<String> permissions = new ArrayList<>();
		permissions.addAll(getGroupsPermission(groups));
		permissions.addAll(iup.getUserPermission());

		List<String> deniedPermissions = new ArrayList<>();
		deniedPermissions.addAll(getGroupsDeniedPermission(groups));
		deniedPermissions.addAll(iup.getUserDeniedPermission());

		return hasPermission(permissions, deniedPermissions, permission);

	}

	public boolean hasPositivePermission(BasicUser iup, List<String> neededPermission) {
		List<String> groups = getGroups(iup);

		List<String> permissions = new ArrayList<>();
		permissions.addAll(getGroupsPermission(groups));
		permissions.addAll(iup.getUserPermission());

		return hasPermission(permissions, new ArrayList<>(), neededPermission);

	}

	public boolean hasNegativePermission(BasicUser iup, List<String> neededPermission) {

		List<String> groups = getGroups(iup);

		List<String> deniedPermissions = new ArrayList<>();
		deniedPermissions.addAll(getGroupsDeniedPermission(groups));
		deniedPermissions.addAll(iup.getUserDeniedPermission());

		return !hasPermission(registeredPermissions, deniedPermissions, neededPermission);

	}

	//check each needed Perm if present and not denied
	boolean hasPermission(List<String> presentPermissions, List<String> deniedPermission, List<String> neededPermissions) {
		for(String p : neededPermissions) {
			if(!hasPermission(presentPermissions, deniedPermission, p)) {
				return false;
			}
		}
		return true;
	}


	@SuppressWarnings({"unchecked", "MethodWithMultipleReturnPoints"})
	//check if permission is present and not denied
	boolean hasPermission(List<String> presentPermissions, List<String> deniedPermission, String permission) {

		for(String p : deniedPermission) {
			if(includesPermission(p, permission)) {
				return false;
			}
		}

		for(String p : presentPermissions) {
			if(includesPermission(p, permission)) {
				return true;
			}
		}

		return false;
	}

	@SuppressWarnings({"BooleanMethodNameMustStartWithQuestion", "OverlyComplexMethod"})
	//is checked included in present
	private boolean includesPermission(String present, String checked) {
		//null will never match
		if(present == null || checked == null) {
			return false;
		}

		//equivalent match
		if(present.equals(checked)) {
			return true;
		}
		//if it does't contain a wild card an didn't already match it does not fit
		if(!present.contains("*")){
			return false;
		}

		String[] permissionArray = present.split(".");
		String[] permArray = checked.split(".");

		//if present is shorter than checked and presents last sub-permission is not a wild card it does not match
		if((permissionArray.length<permArray.length)&&!permissionArray[present.length()-1].equals("*")){
			return false;
		}

		//present is more specific than checked therefore no match
		if(permissionArray.length>permArray.length){
			return false;
		}

		for(int i = 0; i < permissionArray.length && i < permArray.length; i++) {
			//wildcard on present always matches skipp compare
			if(permissionArray[i].equals("*")) {
				continue;
			}

			//no match -> no match
			if(!permissionArray[i].equals(permArray[i])) {
				return false;
			}

		}
		//you made it till here even thou you are not an exact match congratulations you match as well
		return true;
	}

	@SuppressWarnings("StringConcatenationMissingWhitespace")
	public synchronized void writeToFileWriter(FileWriter fileWriter) {
		fileWriter.add(registeredPermissions.size(), "RPS");
		for(int i = 0; i < registeredPermissions.size(); i++) {
			//noinspection StringConcatenation
			fileWriter.add(registeredPermissions.get(i), "PR" + i);
		}
		fileWriter.add(registeredGroups.size(), "RGS");
		for(int i = 0; i < registeredGroups.size(); i++) {
			//noinspection StringConcatenation
			fileWriter.add(registeredGroups.get(i), "RG" + i);
		}
	}

	@SuppressWarnings("StringConcatenationMissingWhitespace")
	public synchronized void loadFromFileWriter(FileWriter fileWriter) {
		if(fileWriter == null) {
			return;
		}
		registeredPermissions.clear();
		registeredGroups.clear();

		int rps = (int) fileWriter.get("RPS");
		int rgs = (int) fileWriter.get("RGS");

		for(int i = 0; i < rps; i++) {
			//noinspection StringConcatenation
			registeredPermissions.add((String) fileWriter.get("PR" + i));
		}
		for(int i = 0; i < rgs; i++) {
			Group g = new Group();
			//noinspection StringConcatenation
			g.loadFromFileWriter((FileWriter) fileWriter.get("RG" + i));
			registeredGroups.add(g);
		}

	}

	public void executePermissionCommand(IChat iChat,IIOHandler uRC,String cmd){
		//noinspection DuplicateStringLiteralInspection
		ICommand command = iChat.getCommandRegistry().getCommand("permission");
		if(command instanceof Permission) {
			for(SubPermission sp : ((Permission) command).subCommandList) {
				if(sp.getName().equals(cmd.split(" ",2)[0].replace(ICommand.COMMAND_INIT_STRING,""))) {
					sp.executePermissionCommand(iChat,uRC,cmd);
					return;
				}
			}
		} else {
			throw new RuntimeException("Did not get Permission command as requested!");
		}
	}

	@SuppressWarnings("ClassNamingConvention")
	private static class Group implements ISaveAble {

		public final List<String> groupPerm = new ArrayList<>();
		public final List<String> groupDeniedPerm = new ArrayList<>();
		public final List<String> groups = new ArrayList<>();
		public       String       name;

		@SuppressWarnings("StringConcatenationMissingWhitespace")
		@Override
		public synchronized void writeToFileWriter(FileWriter fileWriter) {
			fileWriter.add(name, "NAME");
			fileWriter.add(groupPerm.size(), "GPS");
			for(int i = 0; i < groupPerm.size(); i++) {
				//noinspection StringConcatenation
				fileWriter.add(groupPerm.get(i), "GP" + i);
			}
			fileWriter.add(groupDeniedPerm.size(), "GDPS");
			for(int i = 0; i < groupDeniedPerm.size(); i++) {
				//noinspection StringConcatenation
				fileWriter.add(groupDeniedPerm.get(i), "GDP" + i);
			}
			fileWriter.add(groups.size(), "GS");
			for(int i = 0; i < groups.size(); i++) {
				//noinspection StringConcatenation
				fileWriter.add(groups.get(i), "G" + i);
			}
		}

		@SuppressWarnings("StringConcatenationMissingWhitespace")
		@Override
		public synchronized void loadFromFileWriter(FileWriter fileWriter) {
			groupPerm.clear();
			groupDeniedPerm.clear();
			groups.clear();

			name = (String) fileWriter.get("NAME");
			int gps = (int) fileWriter.get("GPS");
			int gdps = (int) fileWriter.get("GDPS");
			int gs = (int) fileWriter.get("GS");
			for(int i = 0; i < gps; i++) {
				//noinspection StringConcatenation
				groupPerm.add((String) fileWriter.get("GP" + i));
			}
			for(int i = 0; i < gdps; i++) {
				//noinspection StringConcatenation
				groupDeniedPerm.add((String) fileWriter.get("GDP" + i));
			}
			for(int i = 0; i < gs; i++) {
				//noinspection StringConcatenation
				groups.add((String) fileWriter.get("G" + i));
			}
		}
	}

}
