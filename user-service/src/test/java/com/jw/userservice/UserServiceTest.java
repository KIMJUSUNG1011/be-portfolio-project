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

        String redis = null;

        if (result) {
            session = new MockHttpSession();
            session.setAttribute("email", requestDto.getEmail());

            request = new MockHttpServletRequest();
            request.setSession(session);

            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set("test_key", "test_value");
            redis = (String)operations.get("test_key");
        }

        Assertions.assertThat(result).isEqualTo(true);
        Assertions.assertThat(redis).isEqualTo("test_value");
    }
}
