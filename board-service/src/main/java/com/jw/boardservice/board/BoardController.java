package com.jw.boardservice.board;

import com.jw.boardservice.board.BoardDto.BoardEditRequestDto;
import com.jw.boardservice.board.BoardDto.BoardListResponseDto;
import com.jw.boardservice.board.BoardDto.BoardReadResponseDto;
import com.jw.boardservice.board.BoardDto.BoardWriteRequestDto;
import com.jw.session.SessionDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class BoardController
{
    private final BoardService boardService;

    @PostMapping(value = "/write", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Long> write(@AuthenticationPrincipal SessionDetails sessionDetails,
                                      @RequestPart BoardWriteRequestDto requestDto,
                                      @RequestPart(value = "files", required = false) List<MultipartFile> files) throws Exception
    {
        Long id = boardService.write(sessionDetails.getEmail(), requestDto, files);

        return ResponseEntity.status(HttpStatus.CREATED).body(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> edit(Principal principal, @PathVariable("id") Long id, @RequestBody BoardEditRequestDto requestDto)
    {
        Boolean result = boardService.edit(principal.getName(), id, requestDto);

        if (result)
            return ResponseEntity.status(HttpStatus.CREATED).body(id);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(Principal principal, @PathVariable("id") Long id)
    {
        Boolean result = boardService.delete(principal.getName(), id);

        if (!result)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardReadResponseDto> read(HttpServletRequest request,
                                                     HttpServletResponse response,
                                                     @PathVariable("id") Long id)
    {
        Cookie cookie = hasLatestViewCookie(request.getCookies(), String.valueOf(id));
        BoardReadResponseDto readResponseDto = boardService.read(cookie, id);

        if (readResponseDto == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        if (cookie != null)
            response.addCookie(cookie);

        return ResponseEntity.status(HttpStatus.OK).body(readResponseDto);
    }

    @GetMapping("/")
    public ResponseEntity<List<BoardListResponseDto>> list()
    {
        List<BoardListResponseDto> boardList = boardService.list();

        if (boardList == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        return ResponseEntity.status(HttpStatus.OK).body(boardList);
    }

    @GetMapping("/{board_id}/{comment_id}/{is_like}")
    public ResponseEntity<String> likeOrDislike(@AuthenticationPrincipal SessionDetails sessionDetails,
                                                @PathVariable("board_id") Long boardId,
                                                @PathVariable("comment_id") Long commentId,
                                                @PathVariable("is_like") Boolean isLike)
    {
        Boolean result = boardService.likeOrDislike(sessionDetails.getId(), boardId, commentId, isLike);

        if (!result)
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(null);

        if (!isLike)
            return ResponseEntity.status(HttpStatus.OK).body("비추천이 완료되었습니다.");

        return ResponseEntity.status(HttpStatus.OK).body("추천이 완료되었습니다.");
    }

    private Cookie hasLatestViewCookie(Cookie[] cookies, String id)
    {
        final String name = "latestView";

        for(Cookie cookie : cookies)
        {
            if(!cookie.getName().equals(name))
                continue;

            // 가장 최근 읽은 글일 때
            if(cookie.getValue().equals(id))
                return null;

            break;
        }

        return new Cookie(name, id);
    }
}
