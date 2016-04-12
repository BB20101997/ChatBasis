package bb.chat.events;

import bb.util.file.log.BBLogHandler;
import bb.util.file.log.Constants;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
		log.addHandler(new BBLogHandler(Constants.getLogFile("ChatBasis")));
	}



	public void subscribeEvent(Method method) {
		log.finer("Subscribing to an Event");
		if(method.getParameterTypes().length == 1 && method.getParameterTypes()[0].isAssignableFrom(Event.class)) {
			if(!mList.contains(method)) {
				mList.add(method);
			}
		} else {
			throw new IllegalArgumentException("Method has to have one Parameter,either of type Event.class or a Subtype!");
		}
	}

	public void distributeEvent(Event event) {
		log.finer("Distributing an Event");
		mList.stream().filter(me -> me.getParameterTypes()[0].isAssignableFrom(event.getClass())).forEach(me -> {
			try {
				me.invoke(event);
			} catch(IllegalAccessException | InvocationTargetException e1) {
				e1.printStackTrace();
			}
		});

	}


}
