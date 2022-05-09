package com.jw.boardservice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.jw.boardservice.BoardDto.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/board-service")
public class BoardController
{
    private final BoardService boardService;

    @PostMapping("/write")
    public ResponseEntity<String> write(HttpSession session, @RequestBody BoardWriteRequestDto requestDto)
    {
        return null;
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> edit(HttpSession session, @RequestParam("id") Long id, @RequestBody BoardEditRequestDto requestDto)
    {
        return null;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(HttpSession session, @RequestParam("id") Long id)
    {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> read(@RequestParam("id") Long id)
    {
        return null;
    }

    @GetMapping("/")
    public ResponseEntity<List<Board>> list()
    {
        return null;
    }
}
