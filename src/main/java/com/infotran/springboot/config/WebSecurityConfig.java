package com.infotran.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.infotran.springboot.commonmodel.AuthUserDetailsService;


@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthUserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
		   .userDetailsService(userDetailsService)
		   .passwordEncoder(new BCryptPasswordEncoder());
		

	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		
	}
	
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
    }
	
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http
		.authorizeRequests()
		.antMatchers("/login/page","/logout/page", "/logout", "/login/welcome").permitAll()
		.antMatchers("/loginSystem/normaluser").hasAnyAuthority("normal" , "admin")
		.antMatchers("/user/**").hasAnyAuthority("normal" , "admin")
		.antMatchers("/shoppingSystem/**").hasAnyAuthority("normal" , "admin")
		.antMatchers("/userView/**").hasAnyAuthority("normal" , "admin")
		.antMatchers("/loginSystem/companyuser").hasAnyAuthority("company" , "admin")
		.antMatchers("/loginSystem/admin").hasAnyAuthority("admin")
		.anyRequest().permitAll()
        .and()
        .formLogin().loginPage("/login/page").defaultSuccessUrl("/login/welcome")
        .and()
        .logout().logoutUrl("/logout/page").logoutSuccessUrl("/logout")
        .and()
        .exceptionHandling().accessDeniedPage("/loginSystem/403")
		.and()
		.csrf().disable()
		.rememberMe().tokenValiditySeconds(1200).key("rememberMe");
		http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());
        


//      .formLogin()
//      .formLogin().loginPage("/login/page")
//      .formLogin().loginPage("/login_page").loginProcessingUrl("/perform_login").failureUrl("/login_page?error")
//        .logout()
//        .logout().logoutUrl("/logout/page")
//		
//		.antMatchers(HttpMethod.GET,"/users/**","/loginSystem/**").authenticated()
//		.antMatchers(HttpMethod.GET).permitAll()
//		.antMatchers(HttpMethod.POST,"/users/**" ,"/loginSystem/**").authenticated()
//		.antMatchers(HttpMethod.POST).permitAll()
//		.anyRequest().authenticated()
//		.and()
//		.formLogin().disable()
//		.httpBasic();
		
//		.formLogin().loginPage("/login/page").defaultSuccessUrl("/login/welcome");

//		.antMatchers(HttpMethod.POST,"/PepperNoodles/**").authenticated()
//		.antMatchers(HttpMethod.POST).permitAll()
//		.anyRequest().authenticated()
		
//		.and()
//		.csrf().disable()

	}
	


}
