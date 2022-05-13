package com.jw.userservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
            return UserEntity.builder().email(email).password(password).build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserUpdateRequestDto
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
    public static class UserWithdrawRequestDto
    {
        private String email;
    }

    @Getter
    @NoArgsConstructor
    public static class UserResponseDto
    {
        private String email;
        private String password;
        private String name;
        private String phone;

        public UserResponseDto(UserEntity entity)
        {
            email = entity.getEmail();
            password = entity.getPassword();
            name = entity.getName();
            phone = entity.getPhone();
        }
    }
}
