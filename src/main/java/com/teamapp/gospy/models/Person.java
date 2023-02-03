package com.teamapp.gospy.models;


import java.util.Date;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.Searchable;
import org.springframework.data.redis.core.RedisHash;

@Document
public class Person {
    // Id Field, also indexed
    @Id
    @Indexed
    private String id;

    // Indexed for exact text matching
    @Indexed

    private String name;

    @Indexed
    private String deviceToken;

    //Indexed for Geo Filtering
    @Indexed
    private Point location;

    @Indexed
    private Date locationUpdated;

    protected Person(String name){
        this.name = name;
        this.id = null;
        this.deviceToken = null;
        this.location = null;
        this.locationUpdated = null;
    }
    public static Person of(String name){
        return new Person(name);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public Date getLocationUpdated() {
        return locationUpdated;
    }

    public void setLocationUpdated(Date locationUpdated) {
        this.locationUpdated = locationUpdated;
    }
}