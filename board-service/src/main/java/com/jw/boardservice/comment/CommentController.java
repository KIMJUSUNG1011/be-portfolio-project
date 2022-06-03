package com.jw.boardservice.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static com.jw.boardservice.comment.CommentDto.*;

@RestController
@RequiredArgsConstructor
public class CommentController
{
    private final CommentService commentService;

    @PostMapping("/{board_id}/comment")
    public ResponseEntity<String> write(Principal principal,
                                        @PathVariable("board_id") Long boardId,
                                        @RequestBody CommentWriteRequestDto requestDto)
    {
        Long commentId = commentService.write(boardId, principal.getName(), requestDto);

        if (commentId == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commentId + "번 댓글 생성에 성공했습니다.");
    }

    @PostMapping("/{board_id}/{comment_id}/comment")
    public ResponseEntity<String> write(Principal principal,
                                        @PathVariable("board_id") Long boardId,
                                        @PathVariable("comment_id") Long commentId,
                                        @RequestBody CommentWriteRequestDto requestDto)
    {
        Long reCommentId = commentService.write(boardId, commentId, principal.getName(), requestDto);

        if (reCommentId == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.CREATED).body(boardId + "번 댓글에 대한 " + commentId + "번 대댓글 생성에 성공했습니다.");
    }

    @PutMapping("/{id}/comment")
    public ResponseEntity<String> edit(Principal principal, @PathVariable Long id, @RequestBody CommentEditRequestDto requestDto)
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
        if (!result)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("댓글 삭제에 실패했습니다.");

        return ResponseEntity.status(HttpStatus.OK).body("댓글이 삭제되었습니다.");
    }
}
