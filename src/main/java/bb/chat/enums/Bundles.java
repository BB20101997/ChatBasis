package bb.chat.enums;

import java.util.ResourceBundle;

/**
 * Created by BB20101997 on 08. Aug. 2016.
 */
public enum Bundles {
	BUTTON_LABEL("bb.chat.lang.ButtonLabel"),COMMAND("bb.chat.lang.Command"),LOG_TEXT("bb.chat.lang.LogText"),MESSAGE("bb.chat.lang.Message");

	public final String BUNDLE_NAME;

	Bundles(String name){
		BUNDLE_NAME = name;
	}

	public ResourceBundle getResource(){
		return ResourceBundle.getBundle(BUNDLE_NAME);
	}
}
