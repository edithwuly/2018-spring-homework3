package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

@Configuration
@EnableWebSecurity 
@EnableOAuth2Client
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter { 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable();  
        http.authorizeRequests()  
            .antMatchers("/oauth/authorize").authenticated()  
            .and()  
            .httpBasic().realmName("OAuth Server");  
    	http 
        .formLogin()
        	.defaultSuccessUrl("/Wordladder")
            .permitAll()
            .and()
        .logout()
            .permitAll();
    }
   
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth 
            .inMemoryAuthentication()
                .withUser("user").password("{noop}123456").roles("USER");
    }
    
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
  
    @Configuration  
    @EnableAuthorizationServer  
    static class OAuthAuthorizationConfig extends AuthorizationServerConfigurerAdapter {  
        @Override  
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {  
            clients.inMemory()  
                    .withClient("client")  
                    .secret("{noop}secret")  
                    .resourceIds("my_resource")  
                    .scopes("read", "write")  
                    .authorities("ROLE_USER")  
                    .authorizedGrantTypes("authorization_code", "refresh_token")  
                    .redirectUris("http://localhost:8080/Wordladder")  
                    .accessTokenValiditySeconds(60*30) // 30min  
                    .refreshTokenValiditySeconds(60*60*24); // 24h  
        }  
    }  
      
    @Configuration  
    @EnableResourceServer  
    static class OAuthResourceConfig extends ResourceServerConfigurerAdapter {  
        @Override  
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {  
            resources.resourceId("my_resource");  
        }  
        @Override  
        public void configure(HttpSecurity http) throws Exception {  
            http.authorizeRequests()  
                .antMatchers(HttpMethod.GET, "/Wordladder").access("#oauth2.hasScope('read')");   
        }  
    } 
}