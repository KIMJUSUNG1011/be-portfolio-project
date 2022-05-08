package com.jw.userservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

import static com.jw.userservice.UserDto.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-service")
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
        if (result)
            session.setAttribute(requestDto.getEmail(), requestDto.getEmail());

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@RequestBody UserUpdateRequestDto requestDto)
    {
        Boolean result = userService.update(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<Boolean> withdraw(@RequestBody UserWithdrawRequestDto requestDto)
    {
        Boolean result = userService.withdraw(requestDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(result);
    }
}
