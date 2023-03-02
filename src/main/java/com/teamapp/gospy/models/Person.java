package com.teamapp.gospy.models;


import java.util.Date;

import com.redis.om.spring.annotations.Searchable;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import org.springframework.data.keyvalue.annotation.KeySpace;
import org.springframework.data.redis.core.RedisHash;

@Document
//@KeySpace("Person") // this is used (among others) to avoid having as keyspace the full qualified class name
public class Person {
    // Id Field, also indexed
    @Id
    @Indexed
    private String entityId;

    // Indexed for exact text matching
    @Indexed
    private String name;

    @Indexed
    private String deviceToken;

    //Indexed for Geo Filtering
    @Indexed
    private Location location;

    @Indexed
    private Date locationUpdated;

    public Person(){
        this.entityId = null;
        this.deviceToken = null;
        this.location = null;
        this.locationUpdated = null;
    }

    protected Person(String name){
        this.name = name;
        this.entityId = null;
        this.deviceToken = null;
        this.location = null;
        this.locationUpdated = null;
    }
    public static Person of(String name){
        return new Person(name);
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Date getLocationUpdated() {
        return locationUpdated;
    }

    public void setLocationUpdated(Date locationUpdated) {
        this.locationUpdated = locationUpdated;
    }

    @Override
    public String toString(){
        return "id=" + this.entityId + ",name=" + this.name +",location=" +
                this.location.longitude + "/" + this.location.latitude +
                ",locationUpdated=" + this.locationUpdated + ",deviceToken=" + this.deviceToken;
    }
}