package com.jw.userservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

import static com.jw.userservice.UserDto.UserRegisterRequestDto;
import static com.jw.userservice.UserDto.UserUpdateRequestDto;

@RestController
@RequiredArgsConstructor
public class UserController
{
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UserRegisterRequestDto requestDto)
    {
        String email = userService.register(requestDto);

        if (email == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(email);
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(Principal principal, @RequestBody UserUpdateRequestDto requestDto)
    {
        Boolean result = userService.update(principal.getName(), requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<Boolean> withdraw(Principal principal, @RequestBody Map<String, String> map)
    {
        String rawPassword = map.get("password");

        Boolean result = userService.withdraw(principal.getName(), rawPassword);

        if (!result)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);

        // remove session
        SecurityContextHolder.clearContext();

        return ResponseEntity.status(HttpStatus.OK).body(true);
    }
}
