package com.teamapp.gospy.models;

import com.redis.om.spring.repository.RedisDocumentRepository;
import com.redis.om.spring.client.RedisModulesClient;

public interface PersonRepository extends RedisDocumentRepository<Person,String> {

}