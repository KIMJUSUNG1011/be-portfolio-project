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
        Long id = userService.register(requestDto);

        if (id == null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 회원입니다.");

        return ResponseEntity.status(HttpStatus.CREATED).body("회원 가입에 성공하였습니다.");
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(Principal principal, @RequestBody UserUpdateRequestDto requestDto)
    {
        Boolean result = userService.update(principal.getName(), requestDto);

        if (!result)
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("사용자 정보 없데이트에 실패했습니다.");

        return ResponseEntity.status(HttpStatus.CREATED).body("사용자 정보가 업데이트 되었습니다.");
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
