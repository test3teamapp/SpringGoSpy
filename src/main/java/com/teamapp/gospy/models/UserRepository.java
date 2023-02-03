package com.teamapp.gospy.models;

import com.redis.om.spring.repository.RedisDocumentRepository;

public interface UserRepository extends RedisDocumentRepository<User,String> {

}