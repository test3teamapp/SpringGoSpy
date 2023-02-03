package com.teamapp.gospy.models;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.Date;

@Document
public class User {

    @Id
    @Indexed
    public String id;

    @Indexed
    public String name;
    @Indexed
    public String pass;
    @Indexed
    public String token;
    @Indexed
    public String chat;
    @Indexed
    public String expires;
    @Indexed
    public Date lastlogin;

    protected User(String name, String pass) {
        this.name = name;
        this.pass = pass;
        this.lastlogin = null;
        this.token = "loggedout";
        this.expires = "never";
        this.chat = "offline";
        this.id = null;
    }

    public static User of(String name, String pass) {
        return new User(name, pass);
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

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public Date getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(Date lastlogin) {
        this.lastlogin = lastlogin;
    }
}
