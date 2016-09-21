package bb.chat.basis;

import bb.chat.enums.Bundles;
import bb.util.file.log.BBLogHandler;

import java.text.MessageFormat;
import java.util.logging.Logger;

import static bb.util.file.log.Constants.getLogFile;

/**
 * Created by BB20101997 on 09. Aug. 2016.
 */
@SuppressWarnings("ClassNamePrefixedWithPackageName")
public class BasisConstants {

	public static final String   LOG_NAME           = "ChatBasis";
	public static final String[] EMPTY_STRING_ARRAY = new String[0];

	@SuppressWarnings("ConstantNamingConvention")
	public static final String ALL       = "ALL";
	public static final String CLIENT    = "Client";
	public static final String SERVER    = "Server";
	public static final String SERVER_UP = SERVER.toUpperCase();

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger LOGGER = getLogger(BasisConstants.class);

	public static Logger getLogger(Class clazz) {
		Logger log = Logger.getLogger(clazz.getName());
		log.addHandler(new BBLogHandler(getLogFile(LOG_NAME)));
		if(LOGGER != null) {
			LOGGER.fine(MessageFormat.format(Bundles.LOG_TEXT.getString("log.logger.new"), LOG_NAME));
		}
		return log;
	}

}
