package bb.chat.base;

import bb.util.file.log.BBLogHandler;

import java.util.logging.Logger;

import static bb.util.file.log.Constants.getLogFile;

/**
 * Created by BB20101997 on 09. Aug. 2016.
 */
public class Constants {

	public final static String LOG_NAME = "ChatBasis";
	public final static String[] EMPTY_STRING_ARRAY = new String[0];

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger LOGGER = getLogger(Constants.class);

	public static Logger getLogger(Class clazz){
		Logger log = Logger.getLogger(clazz.getName());
		log.addHandler(new BBLogHandler(getLogFile(LOG_NAME)));
		if(LOGGER!=null){
			LOGGER.fine("Creating new Logger for the Basis Module!");
		}
		return log;
	}

}
