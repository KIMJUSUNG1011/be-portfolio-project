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
    public ResponseEntity<Long> write(HttpSession session, @RequestBody BoardWriteRequestDto requestDto)
    {
        String email = (String)session.getAttribute("email");
        Long id = boardService.write(email, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> edit(HttpSession session, @RequestParam("id") Long id, @RequestBody BoardEditRequestDto requestDto)
    {
        String email = (String)session.getAttribute("email");
        Boolean result = boardService.edit(id, email, requestDto);

        if (result) {
            return ResponseEntity.status(HttpStatus.CREATED).body(id);
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(id);
        }
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
