package com.jw.userservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.jw.userservice.UserDto.UserRegisterRequestDto;
import static com.jw.userservice.UserDto.UserUpdateRequestDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest
{
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void register()
    {
        // given
        UserRegisterRequestDto registerRequestDto = getUserRegisterRequestDto();
        UserEntity userEntity = getRegisterUserEntity(registerRequestDto);

        final Long fakeId = 1L;
        final String email = "dlrtls";

        // userEntity 의 id 필드 주입
        ReflectionTestUtils.setField(userEntity, "id", fakeId);

        // mocking
        given(passwordEncoder.encode(any())).willReturn("");
        given(userRepository.save(any())).willReturn(userEntity);
        given(userRepository.findByEmail(email)).willReturn(Optional.ofNullable(null));

        // when
        Long newId = userService.register(registerRequestDto);

        assertEquals(newId, fakeId);
    }

    @Test
    void update()
    {
        // given
        UserRegisterRequestDto registerRequestDto = getUserRegisterRequestDto();
        UserUpdateRequestDto updateRequestDto = getUserUpdateRequestDto();
        UserEntity userEntity = getRegisterUserEntity(registerRequestDto);

        final String email = "dlrtls";

        // mocking
        given(userRepository.findByEmail(any())).willReturn(Optional.of(userEntity));

        Boolean result = userService.update(email, updateRequestDto);

        assertEquals(result, true);
    }

    @Test
    void withdraw()
    {
        // given
        UserRegisterRequestDto registerRequestDto = getUserRegisterRequestDto();
        UserEntity userEntity = getRegisterUserEntity(registerRequestDto);

        final String email = "dlrtls";
        final String password = "123";

        // mocking
        given(userRepository.findByEmail(any())).willReturn(Optional.of(userEntity));
        given(passwordEncoder.matches(any(), any())).willReturn(true);

        userService.withdraw(email, password);
    }

    private UserEntity getRegisterUserEntity(UserRegisterRequestDto registerRequestDto)
    {
        return registerRequestDto.toEntity(registerRequestDto.getPassword());
    }

    private UserRegisterRequestDto getUserRegisterRequestDto()
    {
        UserRegisterRequestDto userRegisterRequestDto = new UserRegisterRequestDto();
        userRegisterRequestDto.setEmail("dlrtls");
        userRegisterRequestDto.setPassword("123");
        userRegisterRequestDto.setName("kim");
        userRegisterRequestDto.setPhone("01052927836");
        return userRegisterRequestDto;
    }

    private UserUpdateRequestDto getUserUpdateRequestDto()
    {
        UserUpdateRequestDto userUpdateRequestDto = new UserUpdateRequestDto();
        userUpdateRequestDto.setName("kim2");
        userUpdateRequestDto.setPassword("1234");
        userUpdateRequestDto.setPhone("01012345678");
        return userUpdateRequestDto;
    }
}
