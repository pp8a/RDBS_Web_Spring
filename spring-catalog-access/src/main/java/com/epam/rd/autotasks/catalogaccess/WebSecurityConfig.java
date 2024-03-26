package com.epam.rd.autotasks.catalogaccess;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private static final String MANAGER = "MANAGER";
    private static final String EMPLOYEE = "EMPLOYEE";
    private static final String CUSTOMER = "CUSTOMER";
    
    @Value("${spring.security.csrf.disabled:false}")
	private boolean csrfDisabled;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	 if (csrfDisabled) {
             http
                 .csrf().disable(); // Отключаем CSRF
         }
    	 //It also works 
//    	 http
//         // Другие настройки безопасности...
//         .authorizeRequests()
//         	//only the manager sends requests
//         	 .antMatchers(HttpMethod.POST, "/employees").hasRole(MANAGER) 
//             // forbbiden for CUSTOMER, allow for MANAGER and EMPLOYEE
//              .antMatchers("/employees/**").hasAnyRole(MANAGER, EMPLOYEE)
//             // allow for all roles
//             .antMatchers("/catalog/**").hasAnyRole(MANAGER, EMPLOYEE, CUSTOMER)
//             //available manager or employee
//             .antMatchers("/salaries/my").hasAnyRole(MANAGER, EMPLOYEE)
//             //available only manager
//             .antMatchers("/salaries/**").hasRole(MANAGER)
//             .anyRequest().authenticated()
//             .and()            
//             .httpBasic(); 
    }
}