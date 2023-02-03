package com.teamapp.gospy.models;

import com.redis.om.spring.repository.RedisDocumentRepository;

public interface PersonRepository extends RedisDocumentRepository<Person,String> {

}