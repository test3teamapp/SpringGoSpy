package com.teamapp.gospy.models;

import com.redis.om.spring.annotations.Query;
import com.redis.om.spring.repository.RedisDocumentRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends RedisDocumentRepository<User,String> {
    // Find people by token
    default Optional<User> findOneByToken(String token){
        Iterable<User> allUsers = this.findAll();
        Optional<User> foundUser = Optional.empty();
        for (User user:allUsers) {
            if (user.getToken().compareTo(token) == 0){
                foundUser = Optional.of(user);
                break;
            }
        }
        return foundUser;
    }

    // Find people by name
    /*
    THE SEARCH QUERRY SENT TO DB ALWAYS RETURNED 0 RESULTS.
    "FT.SEARCH" "com.teamapp.gospy.models.UserIdx" "@username:string"
    FUCK IT ! DOING IT THIS WAY TO MOVE ON WITH THE PROJECT

    meanwhile "FT.SEARCH" "com.teamapp.gospy.models.UserIdx" "*"
    retuns as expected all entries.

     */
    default Optional<User> findOneByUsername(String username){
        Iterable<User> allUsers = this.findAll();
        Optional<User> foundUser = Optional.empty();
        for (User user:allUsers) {
            if (user.getUsername().compareTo(username) == 0){
                foundUser = Optional.of(user);
                break;
            }
        }
        return foundUser;
    }

}