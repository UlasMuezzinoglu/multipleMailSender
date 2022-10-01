package com.ulas.springcoretemplate.auth;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserDetailsImp implements UserDetails {

    private final List<? extends GrantedAuthority> grantedAuthorities;
    private final String username;
    private final boolean isAccountNonExpired;
    private final boolean isAccountNonLocked;
    private final boolean isCredentialsNonExpired;
    private final boolean isEnabled;
    private final String password;

    public UserDetailsImp(
            final List<? extends GrantedAuthority> grantedAuthorities,
            final String username,
            final String password,
            final boolean isAccountNonExpired,
            final boolean isAccountNonLocked,
            final boolean isCredentialsNonExpired,
            final boolean isEnabled
    ) {
        this.grantedAuthorities = grantedAuthorities;
        this.username = username;
        this.password = password;
        this.isAccountNonExpired = isAccountNonExpired;
        this.isAccountNonLocked = isAccountNonLocked;
        this.isCredentialsNonExpired = isCredentialsNonExpired;
        this.isEnabled = isEnabled;
    }

    /**
     * this method is get Authorizations as collection in GrantedAuthority extends
     *
     * @return granted Authorizations as collection
     */
    @Override
    public final Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    /**
     * this method is get password
     *
     * @return password as string
     */
    @Override
    public final String getPassword() {
        return password;
    }

    /**
     * this method is get Username
     *
     * @return username as String
     */
    @Override
    public final String getUsername() {
        return username;
    }

    /**
     * this method controls is account non expired
     *
     * @return is Account non expired as boolean
     */
    @Override
    public final boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    /**
     * this method controls is account non locked
     *
     * @return is Account non locked as boolean
     */
    @Override
    public final boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    /**
     * this method controls is Credentials non expired
     *
     * @return is Credentials non expired as boolean
     */
    @Override
    public final boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    /**
     * this method controls is Account Enabled
     *
     * @return is Account enabled as boolean
     */
    @Override
    public final boolean isEnabled() {
        return isEnabled;
    }
}
