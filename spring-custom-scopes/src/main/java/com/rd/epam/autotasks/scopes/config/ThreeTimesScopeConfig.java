package com.rd.epam.autotasks.scopes.config;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.rd.epam.autotasks.factory.ThreeTimesBeanFactoryPostProcessor;

@Configuration
public class ThreeTimesScopeConfig {
	/**
	 * write a Spring configuration class that loads our BeanFactoryPostProcessor implementation
	 * @return
	 */
	@Bean
	public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
		return new ThreeTimesBeanFactoryPostProcessor();
	}

}
