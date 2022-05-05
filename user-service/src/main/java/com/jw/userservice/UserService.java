package com.jw.userservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.jw.userservice.UserDto.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService
{
    private final UserRepository userRepository;

    public String register(UserRegisterRequestDto requestDto)
    {
        User user = userRepository.save(requestDto.toEntity());
        return user.getEmail();
    }

    public Boolean login(UserLoginRequestDto requestDto)
    {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElse(null);

        if (user == null)
        {
            return false;
        }

        return user.getPassword().equals(requestDto.getPassword());
    }
}
