package com.teamapp.gospy.models;

import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import com.redis.om.spring.annotations.Searchable;
import com.teamapp.gospy.helperobjects.Role;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Document
//@KeySpace("User")
public class User implements UserDetails {

    @Id
    @Indexed
    private String id;
    @Indexed
    @Searchable
    private String username;
    @Indexed
    private String password;
    @Indexed
    @Searchable
    private String token;

    @Indexed
    Collection<UserAuthority> authorities;
    @Indexed
    private Date lastlogin;

    public User() {
        this.username = null;
        this.password = null;
        this.lastlogin = null;
        this.token = "loggedout";
        this.id = null;
        this.authorities = null;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.lastlogin = null;
        this.token = "loggedout";
        this.id = null;
        this.authorities = List.of(new UserAuthority(Role.USER));
    }
    public User(boolean isGuest) {
        if (isGuest){
            this.username = "springuser" + RandomStringUtils.randomAlphabetic(5);;
            this.password = "pass" + RandomStringUtils.randomAlphabetic(7);;
            this.lastlogin = null;
            this.token = "loggedout";
            this.id = null;
            this.authorities = List.of(new UserAuthority(Role.GUEST));
        }else {
            this.username = null;
            this.password = null;
            this.lastlogin = null;
            this.token = "loggedout";
            this.id = null;
            this.authorities = null;
        }

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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(Date lastlogin) {
        this.lastlogin = lastlogin;
    }


    @Override
    public Collection<UserAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(Collection<UserAuthority> authorities) {
        this.authorities = authorities;
    }

    public String getPassword() {
        return this.password;
    }

    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
