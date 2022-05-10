package com.jw.boardservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Boolean> delete(HttpSession session, @RequestParam("id") Long id)
    {
        Boolean result = boardService.delete(id);
        if(!result)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Board> read(@RequestParam("id") Long id)
    {
        Board board = boardService.read(id);
        if(board == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(board);
    }

    @GetMapping("/")
    public ResponseEntity<List<Board>> list()
    {
        List<Board> boardList = boardService.list();
        if(boardList == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(boardList);
    }
}
