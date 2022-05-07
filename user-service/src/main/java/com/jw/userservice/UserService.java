package com.jw.userservice;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.jw.userservice.UserDto.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String register(UserRegisterRequestDto requestDto)
    {
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = userRepository.save(requestDto.toEntity(encodedPassword));
        return user.getEmail();
    }

    @Transactional(readOnly = true)
    public Boolean login(UserLoginRequestDto requestDto)
    {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElse(null);

        if (user == null)
            return false;

        return passwordEncoder.matches(requestDto.getPassword(), user.getPassword());
    }

    public Boolean update(UserUpdateRequestDto requestDto)
    {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElse(null);
        if(user == null)
            return false;

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        user.update(requestDto.toEntity(encodedPassword));

        return true;
    }

    public Boolean withdraw(UserWithdrawRequestDto requestDto)
    {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElse(null);
        if(user == null)
            return false;

        userRepository.delete(user);
        return true;
    }
}
