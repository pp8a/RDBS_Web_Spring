package com.epam.rd.autotasks.confbeans.config;

import java.time.LocalDateTime;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Scope;

import com.epam.rd.autotasks.confbeans.video.Channel;
import com.epam.rd.autotasks.confbeans.video.Video;

@Configuration
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Import(SingletonChannelConfig.class)
public class PrototypeChannelConfig {
	@Bean(name="video1")
	public Video video1() {
		return new Video("How to boil water", LocalDateTime.of(2020, 10, 10, 10, 10));
	}	
	@Bean
	public Video video2() {
		return new Video("How to build a house", LocalDateTime.of(2020, 10, 10, 10, 11)); 
	}
	@Bean
	public Video video3() {
		return new Video("How to escape solitude", LocalDateTime.of(2020, 10, 10, 10, 12));
	}
	@Bean
	@Scope("prototype")
	public Channel channel() {
		Channel channel =  new Channel();
		channel.addVideo(video1());
		channel.addVideo(video2());
		channel.addVideo(video3());
		return channel;
	}	
}
