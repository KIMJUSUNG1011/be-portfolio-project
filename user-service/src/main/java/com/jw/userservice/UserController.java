package com.jw.userservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static com.jw.userservice.UserDto.*;

@RestController
@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterRequestDto requestDto)
    {
        String email = userService.register(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(email);
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody UserLoginRequestDto requestDto, HttpSession session)
    {
        Boolean result = userService.login(requestDto);
        if (result) {
            session.setAttribute("email", requestDto.getEmail());
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
