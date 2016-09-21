package bb.chat.events;

import bb.util.event.IEvent;
import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static bb.chat.basis.BasisConstants.LOG_NAME;

/**
 * Created by BB20101997 on 30.01.2015.
 */
//a dummy eventHandler
@SuppressWarnings("unused")
public class EventHandler {
	private final List<Method> mList = new ArrayList<>();

	@SuppressWarnings("ConstantNamingConvention")
	private static final Logger log;

	static {
		log = Logger.getLogger(EventHandler.class.getName());
		//noinspection DuplicateStringLiteralInspection
		log.addHandler(new BBLogHandler(Constants.getLogFile(LOG_NAME)));
	}



	public void subscribeEvent(Method method) {
		log.finer("Subscribing to an IEvent");
		if(method.getParameterTypes().length == 1 && method.getParameterTypes()[0].isAssignableFrom(IEvent.class)) {
			if(!mList.contains(method)) {
				mList.add(method);
			}
		} else {
			throw new IllegalArgumentException("Method has to have one Parameter,either of type IEvent.class or a Subtype!");
		}
	}

	public void distributeEvent(IEvent event) {
		log.finer("Distributing an IEvent");
		mList.stream().filter(me -> me.getParameterTypes()[0].isAssignableFrom(event.getClass())).forEach(me -> {
			try {
				me.invoke(event);
			} catch(IllegalAccessException | InvocationTargetException e1) {
				e1.printStackTrace();
			}
		});

	}


}
