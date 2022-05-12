package com.jw.userservice;

import com.jw.userservice.UserDto.UserLoginRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static com.jw.userservice.UserDto.*;
import static com.jw.userservice.UserDto.UserRegisterRequestDto;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class UserServiceTest
{
    @Autowired
    UserService userService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    MockHttpSession session;
    MockHttpServletRequest request;

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
    void update()
    {
        // given
        UserRegisterRequestDto userRegisterRequestDto = new UserRegisterRequestDto("email", "12345", "이름", "01012345678");
        userService.register(userRegisterRequestDto);
        UserUpdateRequestDto requestDto1 = new UserUpdateRequestDto("email", "54321", "name", "01098765432");
        UserUpdateRequestDto requestDto2 = new UserUpdateRequestDto("email2", "54321", "name", "01098765432");

        // when
        Boolean result1 = userService.update(requestDto1);
        Boolean result2 = userService.update(requestDto2);

        // then
        Assertions.assertThat(result1).isEqualTo(true);
        Assertions.assertThat(result2).isEqualTo(false);
    }

    @Test
    void withdraw()
    {
        // given
        UserRegisterRequestDto userRegisterRequestDto = new UserRegisterRequestDto("email", "12345", "이름", "01012345678");
        userService.register(userRegisterRequestDto);
        UserWithdrawRequestDto requestDto1 = new UserWithdrawRequestDto("email");
        UserWithdrawRequestDto requestDto2 = new UserWithdrawRequestDto("email2");

        // when
        Boolean result1 = userService.withdraw(requestDto1);
        Boolean result2 = userService.withdraw(requestDto2);

        // then
        Assertions.assertThat(result1).isEqualTo(true);
        Assertions.assertThat(result2).isEqualTo(false);
    }
}
