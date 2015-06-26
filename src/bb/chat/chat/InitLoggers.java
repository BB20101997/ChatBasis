package bb.chat.chat;

import bb.util.file.LogHandler;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created by BB20101997 on 12.06.2015.
 */
public class InitLoggers {

	//BasicChatLogges
	public final String BCL = "bb.chat";

	//the Folder to save the logs into
	public  File logFolder = new File("Log").getAbsoluteFile();

	private static InitLoggers inst;

	public static InitLoggers getInstance(){
		if(inst==null){
			inst = new InitLoggers();
		}
		return inst;
	}

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
