package com.rd.epam.autotasks.factory;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import com.rd.epam.autotasks.scopes.ThreeTimesScope;

/**
 * To make the Spring container aware of your new scope, 
 * you need to register it through the register Scope method on a ConfigurableBeanFactory instance.
 */
public class ThreeTimesBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		beanFactory.registerScope("threeTimes", new ThreeTimesScope());
	}

}
