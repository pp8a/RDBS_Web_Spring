package com.epam.rd.autotasks.chesspuzzles.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DefaultBlack.class, DefaultWhite.class})
public class Default {	
	
}
