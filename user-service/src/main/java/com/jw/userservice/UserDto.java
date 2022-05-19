package com.jw.userservice;

import lombok.*;

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
    public static class UserLoginRequestDto
    {
        private String email;
        private String password;

        public UserEntity toEntity()
        {
            return UserEntity.builder()
                    .email(email)
                    .password(password)
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
}
