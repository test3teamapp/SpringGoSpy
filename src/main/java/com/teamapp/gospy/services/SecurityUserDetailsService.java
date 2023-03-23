package com.teamapp.gospy.services;

import com.teamapp.gospy.models.User;
import com.teamapp.gospy.models.UserAuthority;
import com.teamapp.gospy.models.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityUserDetailsService implements UserDetailsService {

    private static Logger logger = LoggerFactory.getLogger(SecurityUserDetailsService.class);
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Optional<User> user = userRepository.findOneByUsername(username);
        //System.out.println("User " + username + " found in DB:" + (!user.isEmpty()));
        if (user.isEmpty()) throw new UsernameNotFoundException("user not found");
        //debug
        //System.out.print("User " + username + " authorities:");
        User tempUser = (User) user.get();
        for (UserAuthority auth: tempUser.getAuthorities()
             ) {
            //System.out.print(auth.getAuthority() + " ");
        }
        //System.out.println();
        return user.get();
    }


}