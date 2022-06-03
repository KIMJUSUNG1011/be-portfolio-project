package com.jw.boardservice.comment;

import com.jw.boardservice.board.Board;
import com.jw.boardservice.board.BoardRepository;
import com.jw.boardservice.likes.Likes;
import com.jw.boardservice.likes.LikesMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.jw.boardservice.comment.CommentDto.*;

@RequiredArgsConstructor
@Service
@Transactional
public class CommentService
{
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final LikesMongoRepository likesMongoRepository;

    public Long write(Long boardId, String email, CommentWriteRequestDto requestDto)
    {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null)
            return null;

        Comment comment = commentRepository.save(requestDto.toEntity(email, board));
        if (comment == null)
            return null;

        board.addComment(comment);

        return comment.getId();
    }

    public Long write(Long boardId, Long parentId, String email, CommentWriteRequestDto requestDto)
    {
        Board board = boardRepository.findById(boardId).orElse(null);
        if (board == null)
            return null;

        Comment parent = commentRepository.findById(parentId).orElse(null);
        if (parent == null || parent.getParent() != null)
            return null;

        Comment comment = commentRepository.save(requestDto.toEntity(email, board, parent));
        if (comment == null)
            return null;

        board.addComment(comment);

        return comment.getId();
    }

    public Boolean edit(String email, Long id, CommentEditRequestDto requestDto)
    {
        Comment comment = commentRepository.findById(id).orElse(null);
        if(comment == null)
            return false;

        if(!email.equals(comment.getEmail()))
            return false;

        comment.update(requestDto.toEntity());
        return true;
    }

    public Boolean delete(String email, Long id)
    {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null)
            return false;

        Likes commentLike = likesMongoRepository.findByCommentId(id).orElse(null);
        if (commentLike == null)
            return false;

        if (!email.equals(comment.getEmail()))
            return false;

        commentRepository.delete(comment);
        likesMongoRepository.delete(commentLike);
        return true;
    }
}
