package bb.chat.enums;

import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import static bb.chat.basis.BasisConstants.LOG_NAME;

/**
 * Created by BB20101997 on 08. Aug. 2016.
 */
public enum Bundles {


	BUTTON_LABEL("bb.chat.lang.ButtonLabel"), COMMAND("bb.chat.lang.Command"), LOG_TEXT("bb.chat.lang.LogText"), MESSAGE("bb.chat.lang.Message");

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger logger;

	static {
		logger = Logger.getLogger(Bundles.class.getName());
		logger.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}

	public final String         BUNDLE_NAME;
	private      Locale         lastLocale;
	private      ResourceBundle bundle;

	Bundles(String name) {
		BUNDLE_NAME = name;
	}

	/**
	 * Refreshes all ResourceBundles of this enum class
	 * */
	public static void refreshAllResources(){
		for(Bundles bundles:Bundles.values()){
			bundles.refreshResource();
		}
	}

	@SuppressWarnings("PublicMethodWithoutLogging")
	public void refreshResource() {
		if((!Locale.getDefault().equals(lastLocale)) || (bundle == null)) {
			lastLocale = Locale.getDefault();
			bundle = ResourceBundle.getBundle(BUNDLE_NAME, lastLocale);
		}
	}


	/**
	 * If bundle is null this methode will call refreshResource()
	 *
	 * @return The ResourceBundle currently in bundle
	 */
	public ResourceBundle getResource() {
		if(bundle==null){
			refreshResource();
		}
		//don't use getResource here will cause infinit recursion!
		logger.fine(MessageFormat.format(LOG_TEXT.getString("log.resource_bundle.get"), BUNDLE_NAME, lastLocale));
		return bundle;
	}

	/**
	 * @param key The key to the value you want to retrieve form the ResourceBundle
	 *
	 * @return the value found in the ResourceBundle under the key
	 */
	@SuppressWarnings("PublicMethodWithoutLogging")
	public String getString(String key) {
		if(bundle==null){
			refreshResource();
		}
		return bundle.getString(key);
	}
}
