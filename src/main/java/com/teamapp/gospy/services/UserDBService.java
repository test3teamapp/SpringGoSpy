package com.teamapp.gospy.services;

import com.teamapp.gospy.models.User;
import com.teamapp.gospy.models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.Optional;

@Service
public class UserDBService {

    @Autowired
    private UserRepository userRepository;
    public UserDBService() {
    }

    public void updateUserLoginDate(String name) {
        Optional<User> user = userRepository.findOneByUsername(name);
        if (user.isPresent()){
            user.get().setLastlogin( new Date());
            userRepository.save(user.get());
        }
    }

    public void updateUserToken( String name, String token) {
        Optional<User> user = userRepository.findOneByUsername(name);
        if (user.isPresent()){
            user.get().setToken(token);
            userRepository.save(user.get());
        }
    }
}
