package bb.chat.events;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BB20101997 on 30.01.2015.
 */
public class EventHandler {

	List<Method> mList = new ArrayList<>();

	public void subscribeEvent(Method method) {
		if(method.getParameterTypes().length == 1 && method.getParameterTypes()[0].isAssignableFrom(Event.class)) {
			if(!mList.contains(method)) {
				mList.add(method);
			}
		} else {
			throw new IllegalArgumentException("Method has to have one Parameter,either of type Event.class or a Subtype!");
		}
	}

	public void distributeEvent(Event event) {
		for(Method m : mList) {
			if(m.getParameterTypes()[0].isAssignableFrom(event.getClass())) {
				try {
					m.invoke(event);
				} catch(IllegalAccessException | InvocationTargetException e1) {
					e1.printStackTrace();
				}
			}
		}

	}


}
