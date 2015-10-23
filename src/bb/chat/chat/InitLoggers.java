package bb.chat.chat;

import bb.util.file.LogHandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.rmi.AccessException;
import java.rmi.UnexpectedException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by BB20101997 on 12.06.2015.
 */

//TODO: WHAT IS THIS EXATCTLY DOING???
public class InitLoggers {

	//BasicChatLogges
	public final String BCL = "bb.chat";

	//the Folder to save the logs into
	private   File logFolder = new File("Log").getAbsoluteFile();

	public void setLogFolder(File f) throws UnexpectedException, NotDirectoryException, AccessException {
		if(isInitialized()){
			throw new UnexpectedException("It´s not allowed to change the logFolder after InitLoggers initialisation!"+System.lineSeparator()+"Sorry!");
		}
		else{
			if(f.isDirectory()) {
				if(f.canWrite()){
					logFolder = f;
				}
				else{
					throw new AccessException("Need write access to logFolder Directory!"+System.lineSeparator()+"Or how do you expect us to write the log File?");
				}
			}
			else{
				throw new NotDirectoryException("Expected a Directory for logFolder!"+System.lineSeparator()+"Is the name not self Explaining?");
			}
		}
	}

	public boolean isInitialized(){
		return inst!=null;
	}

	private static InitLoggers inst;

	public static InitLoggers getInstance(){
		if(inst==null){
			inst = new InitLoggers();
		}
		return inst;
	}

	//00:00 23/24 .10.2015 what am I doing here trying to fix a bug with renaming!

	private InitLoggers(){
		initLogger(BCL,"BasicChat","BasicChat");
	}

	public Logger getLogger(String name){
		return Logger.getLogger(name.intern());
	}

	public void initLogger(String logger,String filename,String loggerName){
		String dateCode = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
		File BCLFile = new File(logFolder, dateCode + "-"+filename+".log");
		try {
			BCLFile.createNewFile();
		} catch(IOException e) {
			e.printStackTrace();
		}
		System.out.println(BCLFile);
		Logger.getLogger(logger.intern()).addHandler(new LogHandler(loggerName,BCLFile));
	}

}
