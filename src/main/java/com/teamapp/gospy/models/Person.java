package com.teamapp.gospy.models;


import java.util.Date;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.Searchable;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@RequiredArgsConstructor(staticName = "of") //makes the creation of a new Person object,
                                            // easy by just calling Person.of (" ", " ", ...)
                                            // with the required arguments
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Data
@Document
@RedisHash("Person")
public class Person {
    // Id Field, also indexed
    @Id
    @Indexed
    private String id;

    // Indexed for exact text matching
    @Indexed @NonNull
    private String name;

    @Indexed @NonNull
    private String deviceToken;

    //Indexed for Geo Filtering
    @Indexed @NonNull
    private Point location;

    @Indexed @NonNull
    private Date locationUpdated;

}