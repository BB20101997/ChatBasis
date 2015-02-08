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

	public void subscribeEvent(Method m) {
		if (m.getParameterTypes().length == 1&& m.getParameterTypes()[0].isAssignableFrom(Event.class)){
			if(!mList.contains(m)){
				mList.add(m);
			}
		}
		else{
			throw new IllegalArgumentException("Methode has to have one Parameter,either of type Event.class or a Subtype!");
		}
	}

	public void distributeEvent(Event e){
		for(Method m : mList){
			if(m.getParameterTypes()[0].isAssignableFrom(e.getClass())){
				try {
					m.invoke(e);
				} catch(IllegalAccessException | InvocationTargetException e1) {
					e1.printStackTrace();
				}
			}
		}

	}


}
