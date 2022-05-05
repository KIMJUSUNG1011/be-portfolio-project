package com.jw.userservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by WOOSERK.
 * User: WOOSERK
 * Date: 2022-05-05
 * Time: 오후 8:18
 */

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

        public User toEntity()
        {
            return new User(null, email, password, name, phone);
        }
    }
}
