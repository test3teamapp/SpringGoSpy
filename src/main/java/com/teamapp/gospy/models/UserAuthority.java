package com.teamapp.gospy.models;

import com.teamapp.gospy.helperobjects.Role;
import org.springframework.security.core.GrantedAuthority;

public class UserAuthority implements GrantedAuthority {
    private Role authority;

    public UserAuthority(Role authority) {
        this.authority = authority;
    }

    public UserAuthority() {
    }

    public void setAuthority(Role authority) {
        this.authority = authority;
    }

    public Role getRole() {
        return this.authority;
    }

    @Override
    public String getAuthority() {
        return this.authority.name();
    }
}
