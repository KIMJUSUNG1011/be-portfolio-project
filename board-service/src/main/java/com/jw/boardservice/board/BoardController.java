package com.jw.boardservice.board;

import com.jw.boardservice.file.FileEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.jw.boardservice.board.BoardDto.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.jw.boardservice.file.FileDto.*;

@RequiredArgsConstructor
@RestController
@Slf4j
public class BoardController
{
    private final BoardService boardService;
    private final Environment env;

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
    public ResponseEntity<BoardReadResponseDto> read(@RequestParam("id") Long id)
    {
        Board board = boardService.read(id);

        List<FileEntity> files = board.getFiles();
        List<FileReadResponseDto> responseFiles = new ArrayList<>();

        String location = env.getProperty("spring.servlet.multipart.location");

        System.out.println("l : " + location);

        System.out.println(files.size());

        for (FileEntity f : files) {
            Path path = Paths.get(location, f.getPath());
            System.out.println(path);
            responseFiles.add(new FileReadResponseDto(
                    f.getName(), path.toString(), f.getSize()
            ));
        }

        BoardReadResponseDto readResponseDto = new BoardReadResponseDto(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getEmail(),
                board.getCount(),
                board.getRegisterDate(),
                responseFiles
        );

        if(board == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);


        return ResponseEntity.status(HttpStatus.OK).body(readResponseDto);
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
