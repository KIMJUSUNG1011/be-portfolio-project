package com.jw.userservice;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static com.jw.userservice.UserDto.UserRegisterRequestDto;

import com.jw.userservice.UserDto.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class UserServiceTest
{
    @Autowired
    UserService userService;

    @Test
    void register()
    {
        // given
        UserRegisterRequestDto userRegisterRequestDto = new UserRegisterRequestDto("email", "12345", "이름", "01012345678");

        // when
        String email = userService.register(userRegisterRequestDto);

        // then
        Assertions.assertThat(email).isEqualTo("email");
    }

    @Test
    void login()
    {
        // given : 이런 환경일 때(미리 선언)
        String email = "email";
        String password = "12345";
        UserRegisterRequestDto userRegisterRequestDto = new UserRegisterRequestDto("email", "12345", "이름", "01012345678");
        userService.register(userRegisterRequestDto);

        // when : 이런 것을 하면(호출)
        UserLoginRequestDto requestDto = new UserLoginRequestDto(email, password);

        // then : 결과
        Boolean result = userService.login(requestDto);

        Assertions.assertThat(result).isEqualTo(true);
    }
}
