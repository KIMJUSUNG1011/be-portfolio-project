package com.jw.boardservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.jw.boardservice.BoardDto.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest
{
    @Mock
    BoardRepository boardRepository;

    @InjectMocks
    BoardService boardService;

    @Test
    void write()
    {
        // given
        String title = "제목1";
        String content = "내용1";
        String email = "dlrtls12345@naver.com";

        BoardWriteRequestDto requestDto = new BoardWriteRequestDto(title, content);

        // mocking
        when(boardRepository.save(requestDto.toEntity(email))).thenReturn(new Board());

        // when
        boardService.write(email, requestDto);
    }

    @Test
    void edit()
    {
        Long id = (long)0;

        String title = "제목2";
        String content = "내용2";
        String email = "dlrtls12345@naver.com";

        BoardEditRequestDto requestDto = new BoardEditRequestDto(title, content);

        Boolean result = boardService.edit(id, email, requestDto);

        assertThat(result).isEqualTo(true);
    }

    @Test
    void delete()
    {
        Board board = new Board(1L, "title", "content", "email", 0);
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));

        // when
        Boolean result = boardService.delete(board.getId());

        // then
        assertThat(result).isEqualTo(true);
    }

    @Test
    void read()
    {
        // given
        Board board = new Board(1L, "title", "content", "email", 0);
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));

        // when
        Board findOne = boardService.read(board.getId());

        // then
        assertThat(findOne.getId()).isEqualTo(board.getId());
    }

    @Test
    void list()
    {
        // given
        Board board1 = new Board(1L, "title", "content", "email", 0);
        Board board2 = new Board(2L, "title", "content", "email", 0);
        ArrayList<Board> list = new ArrayList<>();
        list.add(board1);
        list.add(board2);

        when(boardRepository.findAll()).thenReturn(list);

        // when
        List<Board> findList = boardService.list();

        // then
        assertThat(findList.get(0)).isEqualTo(board1);
        assertThat(findList.get(1)).isEqualTo(board2);
    }
}
