package com.jw.boardservice.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.jw.boardservice.board.BoardDto.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class BoardController
{
    private final BoardService boardService;

    @PostMapping(value = "/write", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Long> write(HttpSession session,
                                      @RequestPart BoardWriteRequestDto requestDto,
                                      @RequestPart(value = "files", required = false) List<MultipartFile> files) throws Exception
    {
        String email = (String)session.getAttribute("email");

        Long id = boardService.write(email, requestDto, files);

        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> edit(HttpSession session, @PathVariable("id") Long id, @RequestBody BoardEditRequestDto requestDto)
    {
        String email = (String)session.getAttribute("email");
        Boolean result = boardService.edit(id, email, requestDto);

        if (result)
            return ResponseEntity.status(HttpStatus.CREATED).body(id);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(HttpSession session, @PathVariable("id") Long id)
    {
        Boolean result = boardService.delete(id);

        if(!result)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardReadResponseDto> read(@PathVariable("id") Long id)
    {
        BoardReadResponseDto readResponseDto = boardService.read(id);

        if(readResponseDto == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(readResponseDto);
    }

    @GetMapping("/")
    public ResponseEntity<List<BoardListResponseDto>> list()
    {
        List<BoardListResponseDto> boardList = boardService.list();

        if(boardList == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(boardList);
    }
}
