package com.jw.userservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class UserDto
{
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRegisterRequestDto
    {
        private String email;
        private String password;
        private String name;
        private String phone;

        public UserEntity toEntity(String encodedPassword)
        {
            return UserEntity.builder()
                    .email(email)
                    .password(encodedPassword)
                    .name(name)
                    .phone(phone)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserUpdateRequestDto
    {
        private String password;
        private String name;
        private String phone;

        public UserEntity toEntity(String encodedPassword)
        {
            return UserEntity.builder()
                    .password(encodedPassword)
                    .name(name)
                    .phone(phone)
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserTokenDto
    {
        private String email;
        private String password;
        private String name;
        private String phone;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserEntityDto implements UserDetails
    {
        private Long id;
        private String email;
        private String password;
        private String name;
        private String phone;
        private ArrayList<GrantedAuthority> authorities;

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities()
        {
            return authorities;
        }

        @Override
        public String getPassword()
        {
            return password;
        }

        @Override
        public String getUsername()
        {
            return email;
        }

        @Override
        public boolean isAccountNonExpired()
        {
            return true;
        }

        @Override
        public boolean isAccountNonLocked()
        {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired()
        {
            return true;
        }

        @Override
        public boolean isEnabled()
        {
            return true;
        }
    }
}
