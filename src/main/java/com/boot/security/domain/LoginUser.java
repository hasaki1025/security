package com.boot.security.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUser implements UserDetails {
    private User user;

    private String tokenId;

    public LoginUser(User user) {
        this.user = user;
    }

    public LoginUser(User user, List<String> authority) {
        this.user = user;
        this.authority = authority;
    }

    //封装权限信息
    private List<String> authority;

    @Override
    //重写获取权限信息方法
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authority.stream().
                map(SimpleGrantedAuthority::new).
                collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return user.getUPassword();
    }

    @Override
    public String getUsername() {
        return user.getUName();
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
