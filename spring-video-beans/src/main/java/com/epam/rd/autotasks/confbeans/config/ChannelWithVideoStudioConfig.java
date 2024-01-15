package com.epam.rd.autotasks.confbeans.config;

import java.time.LocalDateTime;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.epam.rd.autotasks.confbeans.video.Channel;
import com.epam.rd.autotasks.confbeans.video.Video;
import com.epam.rd.autotasks.confbeans.video.VideoStudio;

@Configuration
public class ChannelWithVideoStudioConfig {
	@Bean
	public VideoStudio studio() {
		return new VideoStudio() {
			private int movieNumber = 1;
			@Override
			public Video produce() {
				String movieName =  "Cat & Curious " + movieNumber;	
				LocalDateTime releaseTime = LocalDateTime.of(2001, 10, 18, 10, 00).plusYears(2 * (movieNumber-1));
				movieNumber++;
				
				return new Video(movieName, releaseTime);
			}
		};
	}
	@Bean
	public Channel channel() {
		Channel channel = new Channel();		
		for (int i = 0; i < 8; i++) {
			channel.addVideo(studio().produce());
		}
		return channel;		
	}
	

}
