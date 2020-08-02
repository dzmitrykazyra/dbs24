/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kdg.fs24.entity.security;

import com.kdg.fs24.application.core.service.funcs.ServiceFuncs;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;
import lombok.Data;
import javax.persistence.*;

/**
 *
 * @author Козыро Дмитрий
 */
@Data
@Embeddable
public class ApplicationUserDetails implements UserDetails {

    @Column(name = "login", insertable = false, updatable = false)
    private String username;
    @Column(name = "password", insertable = false, updatable = false)
    private String password;

    @Transient
    private Collection<GrantedAuthority> grantedAuthorities;

    @Transient
    private boolean accountNonExpired = true;
    @Transient
    private boolean accountNonLocked = true;
    @Transient
    private boolean credentialsNonExpired = true;
    @Transient
    private boolean enabled = true;
    //private Collection<String> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if (null == this.grantedAuthorities) {

            final GrantedAuthority grantedAuthority = () -> "ROLE";
            this.grantedAuthorities = ServiceFuncs.<GrantedAuthority>getOrCreateCollection(ServiceFuncs.COLLECTION_NULL);
            this.grantedAuthorities.add(grantedAuthority);

        }

        return this.grantedAuthorities;

    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {

        return accountNonLocked;

    }

    @Override
    public boolean isCredentialsNonExpired() {

        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {

        return enabled;

    }
}
