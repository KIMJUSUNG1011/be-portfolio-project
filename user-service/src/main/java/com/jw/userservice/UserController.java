package com.jw.userservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.jw.userservice.UserDto.*;

@RestController
@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(UserRegisterRequestDto requestDto)
    {
        String email = userService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(email);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login()
    {

    }
}
