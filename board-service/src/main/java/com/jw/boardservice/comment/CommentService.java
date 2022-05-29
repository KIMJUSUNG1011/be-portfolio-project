package com.jw.boardservice.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.jw.boardservice.comment.CommentDto.CommentEditRequestDto;

@RequiredArgsConstructor
@Service
@Transactional
public class CommentService
{
    private final CommentRepository commentRepository;

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
        if(comment == null)
            return false;

        if(!email.equals(comment.getEmail()))
            return false;

        commentRepository.delete(comment);
        return true;
    }
}
