package com.teamapp.gospy.auth;

import com.teamapp.gospy.configuration.WebSecurityConfig;
import com.teamapp.gospy.helperobjects.Role;
import com.teamapp.gospy.models.User;
import com.teamapp.gospy.models.UserAuthority;
import com.teamapp.gospy.models.UserRepository;
import com.teamapp.gospy.services.SecurityUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@Component
public class AuthProvider implements AuthenticationProvider {

    private static Logger logger = LoggerFactory.getLogger(AuthProvider.class);
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SecurityUserDetailsService userDetailsService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        User user = (User) userDetailsService.loadUserByUsername(username);
        // not encypted in db
        String pass = (String) authentication.getCredentials();
        boolean isMatch =  pass.compareTo(user.getPassword()) == 0 ? true : false ;//passwordEncoder.matches((CharSequence) authentication.getCredentials(), user.getPassword());
        logger.info("password matching result : " + isMatch);
        if (isMatch){
            List<Role> roles =  user.getAuthorities().stream()
                    .map(authority -> authority.getRole())
                    .collect(toList());
            List<GrantedAuthority> authorities = Stream.of(roles)
                    .distinct()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(toList());
//            ArrayList<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>() ;
//            for (Role r: roles
//            ) {
//                authorities.add(new SimpleGrantedAuthority("ROLE_" + r.name()));
//            }

            // debug
            logger.debug("Assigned authorities : " + authorities.stream().map(auth -> auth.getAuthority()));
            for (GrantedAuthority auth: authorities
            ) {
                //System.out.print(auth.getAuthority() + " ");
            }
            //System.out.println();
            /// end debug
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, pass, authorities);

            return authToken;
        }else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
