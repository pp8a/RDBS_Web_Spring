package com.rd.epam.autotasks.scopes;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

public class ThreeTimesScope implements Scope {
	/**
	 * One of the first things to consider when implementing a custom Scope class is 
	 * how you will store and manage the scoped objects and destruction callbacks.
	 * we’ll do this in a thread-safe manner using synchronized maps.
	 */
	private Map<String, Runnable> destructionCallbacks = Collections.synchronizedMap(new HashMap<>());
	
	private final Map<String, Integer> beanCountMap = Collections.synchronizedMap(new HashMap<>());
	private final Map<String, Object> beanInstances = Collections.synchronizedMap(new HashMap<>());	
	
	/**
	 * To retrieve an object by name from our scope, let’s implement the getObject method. 
	 * 
	 * we check to see if the named object is in our map. 
	 * If it is, we return it, and if not, we use the ObjectFactory to create a new object, add it to our map, and return it
	 */
	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {		
		int count = beanCountMap.getOrDefault(name, 0);
		if(count < 3) {			
			beanCountMap.put(name, count + 1);		
			// Возвращаем (или создаем) экземпляр бина для данного имени
			return beanInstances.computeIfAbsent(name, k -> objectFactory.getObject());			
		} else {
			 beanCountMap.put(name, 1); // Сбрасываем счетчик, т.к. достигнут лимит				 
			 beanInstances.put(name, objectFactory.getObject()); // При достижении лимита, заменяем объект новым	
			
			 return beanInstances.get(name);// Возвращаем новый (замененный) объект для данного имени		
		}
		
	}
	
	/**
	 * removes the named object from the scope and also removes its registered destruction callback, returning the removed object
	 */
	@Override
	public Object remove(String name) {	
		destructionCallbacks.remove(name);
		return beanInstances.remove(name);
	}

	/**
	 * This method provides a callback that is to be executed when the named object is destroyed
	 * or if the scope itself is destroyed by the application
	 */
	@Override
	public void registerDestructionCallback(String name, Runnable callback) {
		destructionCallbacks.put(name, callback);
	}
	
	/**
	 * If your scope supports multiple contextual objects, 
	 * you would associate each with a key value, 
	 * and you would return the object corresponding to the provided key parameter. 
	 * Otherwise, the convention is to return null
	 */
	@Override
	public Object resolveContextualObject(String key) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * your scope supports the concept of a conversation ID
	 */
	@Override
	public String getConversationId() {
		return "threeTimes";
	}

}
