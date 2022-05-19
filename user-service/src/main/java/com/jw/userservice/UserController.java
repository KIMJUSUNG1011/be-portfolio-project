package com.jw.userservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        if (email == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(email);
    }

    @PutMapping("/update")
    public ResponseEntity<Boolean> update(@RequestHeader String email, @RequestBody UserUpdateRequestDto requestDto)
    {
        Boolean result = userService.update(email, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @DeleteMapping("/withdraw")
    public ResponseEntity<Boolean> withdraw(@RequestHeader String email)
    {
        Boolean result = userService.withdraw(email);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
