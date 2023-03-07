package com.teamapp.gospy.configuration;

import com.teamapp.gospy.services.AuthServiceRedisToken;
import com.teamapp.gospy.services.AuthServiceRedisUser;
import com.teamapp.gospy.services.AuthServiceRedisUserWebLogin;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Optional;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    private static Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
    @Autowired
    AuthServiceRedisToken authServiceToken;
    @Autowired
    AuthServiceRedisUser authServiceUser;
    @Autowired
    AuthServiceRedisUserWebLogin authServiceRedisUserWebLogin;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Auth filter
                //.addFilterAt(this::authenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf().disable()
                .authorizeHttpRequests((requests) -> requests
                        .anyRequest().authenticated()//permitAll()//authenticated()

                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) -> logout
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                );

                // Disable "JSESSIONID" cookies
                //.sessionManagement(conf -> {
                //    conf.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                //});
                // Exception handling
                //.exceptionHandling(conf -> {
                //    conf.authenticationEntryPoint(this::authenticationFailedHandler);
                //});


        return http.build();
    }

    private void authenticationFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        logger.info("Trying to check access with token");
        Optional<Authentication> authentication = this.authServiceToken.authenticate((HttpServletRequest) request);
        if (authentication.isPresent()) {
            logger.info("Logging in with access token");
            authentication.ifPresent(SecurityContextHolder.getContext()::setAuthentication);
        } else {
            logger.info("Trying to check access with user credentials");
            authentication = this.authServiceUser.authenticate((HttpServletRequest) request);
            if (authentication.isPresent()) {
                logger.info("Logging in with user credentials");
                authentication.ifPresent(SecurityContextHolder.getContext()::setAuthentication);
            } else {
                logger.info("Trying to check access with user credentials from web access");
                boolean isAuthenticated = false;
                if (SecurityContextHolder.getContext() != null) {
                    if (SecurityContextHolder.getContext().getAuthentication() != null) {
                        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                            isAuthenticated = true;
                            logger.info("User is already authenticated");
                        }else {
                            logger.info("User is not authenticated");
                        }
                    }else {
                        logger.info("Authentication object in security context is null");
                    }
                }else {
                    logger.info("Security context is null");
                }

                if (! isAuthenticated){
                    authentication = this.authServiceRedisUserWebLogin.authenticate((HttpServletRequest) request);
                    if (authentication.isPresent()) {
                        logger.info("Logging in with user credentials from web access");
                        SecurityContextHolder.getContext().setAuthentication(authentication.get());
                    }
                }

            }

        }

        chain.doFilter(request, response);
    }

    private void authenticationFailedHandler(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        // Trigger the browser to prompt for Basic Auth
        response.setHeader(HttpHeaders.WWW_AUTHENTICATE, "Basic");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }


}