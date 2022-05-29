package com.jw.boardservice.comment;

import com.jw.boardservice.board.Board;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.jw.boardservice.comment.CommentDto.CommentEditRequestDto;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest
{
    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommentService commentService;

    @Test
    @DisplayName("댓글 수정 테스트")
    void edit()
    {
        // given
        Board board = new Board("제목", "내용", "이메일");
        Comment comment = new Comment("일메이", board, "내용");
        CommentEditRequestDto editRequestDto = new CommentEditRequestDto("용내");

        // mocking
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        // when
        Boolean result = commentService.edit("일메이", comment.getId(), editRequestDto);
        Boolean result2 = commentService.edit("", comment.getId(), editRequestDto);

        // then
        assertThat(result).isEqualTo(true);
        assertThat(comment.getContent()).isEqualTo("용내");
        assertThat(result2).isEqualTo(false);
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void delete()
    {
        // given
        Comment comment = new Comment("이메일", null, "내용");
        // mocking
        when(commentRepository.findById(comment.getId())).thenReturn(Optional.of(comment));

        // when
        Boolean result = commentService.delete("이메일", comment.getId());
        Boolean result2 = commentService.delete("일메이", comment.getId());

        // then
        assertThat(result).isEqualTo(true);
        assertThat(result2).isEqualTo(false);
    }
}
