package com.teamapp.gospy.models;

import com.redis.om.spring.repository.RedisDocumentRepository;

import java.util.Optional;

public interface UserRepository extends RedisDocumentRepository<User,String> {
    // Find people by token
    Optional<User> findByToken(String token);

    // Find people by name
    Optional<User> findByName(String name);

}