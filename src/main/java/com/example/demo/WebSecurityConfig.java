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
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableWebSecurity 
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter { 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http 
            .authorizeRequests()
            	.antMatchers("/Wordladder/**").hasRole("USER")
                .anyRequest().authenticated()
                .and()
            .logout()
                .permitAll()
                .and()  
            .httpBasic().realmName("OAuth Server");
        http.csrf().disable(); 
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
    public class OAuth2ServerConfig extends AuthorizationServerConfigurerAdapter {

        @Override
        public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
            oauthServer
                    .tokenKeyAccess("permitAll()") //url:/oauth/token_key,exposes public key for token verification if using JWT tokens
                    .checkTokenAccess("isAuthenticated()") //url:/oauth/check_token allow check token
                    .allowFormAuthenticationForClients();
        }
        
        @Autowired
        private AuthenticationManager authenticationManager;

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints.authenticationManager(authenticationManager);
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.inMemory()
                    .withClient("demoApp")
                    .secret("demoAppSecret")
                    .authorizedGrantTypes("client_credentials", "password", "refresh_token")
                    .scopes("all")
                    .resourceIds("oauth2-resource")
                    .accessTokenValiditySeconds(1200)
                    .refreshTokenValiditySeconds(50000);
        }
    }
      
    @Configuration  
    @EnableResourceServer  
    public class ResourceServerConfig extends ResourceServerConfigurerAdapter {   
        /*@Override  
        public void configure(HttpSecurity http) throws Exception {  
            http.authorizeRequests()  
                .antMatchers(HttpMethod.GET, "/Wordladder").access("#oauth2.hasScope('read')");   
        }  */
    }  
}