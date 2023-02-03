package com.teamapp.gospy.models;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;

@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PROTECTED) // CONSTRUCTOR OF ALL arguments is generated, with Protected access
@Data  //All together now: A shortcut for @ToString, @EqualsAndHashCode, @Getter on all fields, and @Setter on all non-final fields, and @RequiredArgsConstructor!
@Document
@RedisHash("User")
public class User {

    @Id
    @Indexed
    public String id;

    @Indexed @NonNull
    public String name;
    @Indexed @NonNull
    public String pass;
    @Indexed @NonNull
    public String token;
    @Indexed @NonNull
    public String chat;
    @Indexed @NonNull
    public String expires;
    @Indexed
    public Date lastlogin;

    @Override
    public String toString() {
        return String.format(
                "User[id=%s, name='%s', token='%s']",
                id, name, token);
    }

}
