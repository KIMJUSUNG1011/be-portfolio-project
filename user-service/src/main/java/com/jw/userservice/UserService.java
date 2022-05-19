package com.jw.userservice;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static com.jw.userservice.UserDto.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public String register(UserRegisterRequestDto requestDto) {
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        UserEntity user = userRepository.save(requestDto.toEntity(encodedPassword));
        return user.getEmail();
    }

    public Boolean update(UserUpdateRequestDto requestDto) {
        UserEntity user = userRepository.findByEmail(requestDto.getEmail()).orElse(null);
        if (user == null)
            return false;

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        user.update(requestDto.toEntity(encodedPassword));

        return true;
    }

    public Boolean withdraw(UserWithdrawRequestDto requestDto) {
        UserEntity user = userRepository.findByEmail(requestDto.getEmail()).orElse(null);
        if (user == null)
            return false;

        userRepository.delete(user);
        return true;
    }

    public UserEntity findByEmail(String email)
    {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(email).orElse(null);
        if (user == null)
            throw new UsernameNotFoundException(email);

        return new User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }
}
