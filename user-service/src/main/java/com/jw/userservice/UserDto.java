package com.jw.userservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserDto
{
    @Data
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

    @Data
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
}
