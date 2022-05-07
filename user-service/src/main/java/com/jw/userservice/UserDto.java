package com.jw.userservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto
{
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class UserRegisterRequestDto
    {
        private String email;
        private String password;
        private String name;
        private String phone;

        public User toEntity(String encodedPassword)
        {
            return User.builder()
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
    static class UserLoginRequestDto
    {
        private String email;
        private String password;

        public User toEntity()
        {
            return User.builder().email(email).password(password).build();
        }
    }
}
