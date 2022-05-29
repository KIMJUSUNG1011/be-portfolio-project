package com.jw.boardservice.comment;

import com.jw.boardservice.board.Board;
import org.junit.jupiter.api.DisplayName;
import com.jw.boardservice.board.BoardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.jw.boardservice.comment.CommentDto.*;
import static com.jw.boardservice.comment.CommentDto.CommentEditRequestDto;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest
{
    @Mock
    CommentRepository commentRepository;

    @Mock
    private BoardRepository boardRepository;

    @InjectMocks
    CommentService commentService;

    @Test
    void write_댓글()
    {
        CommentWriteRequestDto requestDto = new CommentWriteRequestDto("안녕하세요. 와리가리조쿠 김우석입니다.");
        Board board = new Board("제목", "내용", "dhqpf123");
        Comment comment = requestDto.toEntity("dlrtls", board);

        ReflectionTestUtils.setField(board, "id", 1L);
        ReflectionTestUtils.setField(comment, "id", 1L);

        given(boardRepository.findById(any())).willReturn(Optional.ofNullable(board));
        given(commentRepository.save(any())).willReturn(comment);

        Long id = commentService.write(board.getId(), comment.getEmail(), requestDto);

        assertEquals(id, 1L);
    }

    @Test
    void write_대댓글()
    {
        CommentWriteRequestDto requestDto = new CommentWriteRequestDto("네 저는 정상인 김주성입니다. 댓글 삭제해주세요");
        Board board = new Board("제목", "내용", "dhqpf123");
        Comment parent = new Comment("dlrtls", "와리가리조쿠 김우석입니다.", board, null);
        Comment comment = requestDto.toEntity("jusung012", board, parent);

        ReflectionTestUtils.setField(board, "id", 1L);
        ReflectionTestUtils.setField(parent, "id", 1L);
        ReflectionTestUtils.setField(comment, "id", 1L);

        given(boardRepository.findById(any())).willReturn(Optional.ofNullable(board));
        given(commentRepository.findById(any())).willReturn(Optional.ofNullable(parent));
        given(commentRepository.save(any())).willReturn(comment);

        Long id = commentService.write(board.getId(), parent.getId(), comment.getEmail(), requestDto);

        assertEquals(id, 1L);
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    void edit()
    {
        // given
        Board board = new Board("제목", "내용", "이메일");
        Comment comment = new Comment("일메이", "내용", board, null);
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
    void delete() {
        // given
        Comment comment = new Comment("이메일", "내용", null, null);
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
