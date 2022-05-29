package com.jw.boardservice.board;

import com.jw.boardservice.board.Board;
import com.jw.boardservice.board.BoardRepository;
import com.jw.boardservice.board.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.jw.boardservice.board.BoardDto.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest
{
    @Mock
    BoardRepository boardRepository;

    @InjectMocks
    BoardService boardService;

    @Test
    @DisplayName("글 작성 테스트")
    void write() throws Exception
    {
        // given
        BoardWriteRequestDto boardWriteRequestDto = new BoardWriteRequestDto("제목", "내용");
        Board board = new Board("제목", "내용", "이메일");
        List<MultipartFile> files = new ArrayList<>();

        // mocking
        when(boardRepository.save(any())).thenReturn(board);

        // when
        Long id = boardService.write("이메일", boardWriteRequestDto, files);

        // then
        assertThat(id).isEqualTo(board.getId());
    }

    @Test
    @DisplayName("글 수정 테스트")
    void edit()
    {
        // given
        Board board = new Board("제목", "내용", "이메일");
        BoardEditRequestDto requestDto = new BoardEditRequestDto("제목", "내용");

        // mocking
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));

        // when
        Boolean result = boardService.edit("이메일", board.getId(), requestDto);
        Boolean result2 = boardService.edit("이메일", 100L, requestDto);

        // then
        assertThat(result).isEqualTo(true);
        assertThat(result2).isEqualTo(false);
    }

    @Test
    @DisplayName("글 삭제 테스트")
    void delete()
    {
        Board board = new Board("title", "content", "email");

        // mocking
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));

        // when
        Boolean result = boardService.delete("email", board.getId());
        Boolean result2 = boardService.delete("email", 100L);

        // then
        assertThat(result).isEqualTo(true);
        assertThat(result2).isEqualTo(false);
    }

    @Test
    @DisplayName("쿠키가 없을 때 글 조회 테스트")
    void read()
    {
        // given
        Board board = new Board("title", "content", "email");

        // mocking
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));

        // when
        BoardReadResponseDto readResponseDto = boardService.read(null, board.getId());

        // then
        assertThat(readResponseDto.getId()).isEqualTo(board.getId());
        assertThat(readResponseDto.getCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("쿠키가 있을 때 글 조회 테스트")
    void readWithCookie()
    {
        // given
        Board board = new Board("title", "content", "email");
        Cookie cookie = new Cookie("latestView", String.valueOf(board.getId()));

        // mocking
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));

        // when
        BoardReadResponseDto responseDto = boardService.read(cookie, board.getId());

        // then
        assertThat(responseDto.getId()).isEqualTo(board.getId());
        assertThat(responseDto.getCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("글 목록 테스트")
    void list()
    {
        // given
        Board board1 = new Board("title", "content", "email");
        Board board2 = new Board("title2", "content2", "email2");
        ArrayList<Board> list = new ArrayList<>();
        list.add(board1);
        list.add(board2);

        when(boardRepository.findAll()).thenReturn(list);

        // when
        List<BoardListResponseDto> findList = boardService.list();

        // then
        assertThat(findList.get(0).getTitle()).isEqualTo(board1.getTitle());
        assertThat(findList.get(1).getTitle()).isNotEqualTo(board1.getTitle());
        assertThat(findList.get(1).getTitle()).isEqualTo(board2.getTitle());
    }
}
