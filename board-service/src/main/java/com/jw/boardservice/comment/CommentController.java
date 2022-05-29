package com.jw.boardservice.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.jw.boardservice.comment.CommentDto.*;

@RequiredArgsConstructor
@RestController
@Slf4j
public class CommentController {
    private final CommentService commentService;

    @PutMapping("/{id}/comment")
    public ResponseEntity<String> edit(Principal principal, @PathVariable Long id, CommentEditRequestDto requestDto)
    {
        Boolean result = commentService.edit(principal.getName(), id, requestDto);
        if(!result)
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body("댓글 수정에 실패하였습니다.");

        return ResponseEntity.status(HttpStatus.CREATED).body("댓글 수정이 완료되었습니다.");
    }

    @DeleteMapping("/{id}/comment")
    public ResponseEntity<String> delete(Principal principal, @PathVariable Long id)
    {
        Boolean result = commentService.delete(principal.getName(), id);
        if(!result)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("댓글 삭제에 실패했습니다.");

        return ResponseEntity.status(HttpStatus.OK).body("댓글이 삭제되었습니다.");
    }
}
