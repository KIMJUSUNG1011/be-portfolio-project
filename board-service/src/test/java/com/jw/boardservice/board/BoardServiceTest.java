package com.jw.boardservice.board;

import com.jw.boardservice.comment.Comment;
import com.jw.boardservice.likes.Likes;
import com.jw.boardservice.likes.LikesMongoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.HashSet;
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
    @Mock
    LikesMongoRepository likesMongoRepository;

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
        Likes boardLikes = new Likes(1L, null);
        List<Likes> commentLikesList = new ArrayList<>();
        commentLikesList.add(new Likes(1L, 1L));
        commentLikesList.add(new Likes(1L, 2L));

        // mocking
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));
        when(likesMongoRepository.findByBoardIdAndCommentIdIsNull(any())).thenReturn(Optional.ofNullable(boardLikes));
        when(likesMongoRepository.findAllByBoardIdAndCommentIdIsNotNull(any())).thenReturn(commentLikesList);

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
        List<Likes> likesList = new ArrayList<>();
        likesList.add(new Likes(board.getId(), 0L));

        // mocking
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));
        when(likesMongoRepository.findAllByBoardIdOrderByCommentId(board.getId())).thenReturn(likesList);

        // when
        BoardReadResponseDto readResponseDto = boardService.read(null, board.getId());

        // then
        assertThat(readResponseDto.getId()).isEqualTo(board.getId());
        assertThat(readResponseDto.getCount()).isEqualTo(0);
        assertThat(readResponseDto.getLikes().getBoardId()).isEqualTo(readResponseDto.getId());
    }

    @Test
    @DisplayName("쿠키가 있을 때 글 조회 테스트")
    void readWithCookie()
    {
        // given
        Board board = new Board("title", "content", "email");
        Cookie cookie = new Cookie("latestView", String.valueOf(board.getId()));
        List<Likes> likesList = new ArrayList<>();
        likesList.add(new Likes(board.getId(), 0L));

        // mocking
        when(boardRepository.findById(board.getId())).thenReturn(Optional.of(board));
        when(likesMongoRepository.findAllByBoardIdOrderByCommentId(board.getId())).thenReturn(likesList);

        // when
        BoardReadResponseDto responseDto = boardService.read(cookie, board.getId());

        // then
        assertThat(responseDto.getId()).isEqualTo(board.getId());
        assertThat(responseDto.getCount()).isEqualTo(1);
        assertThat(responseDto.getLikes().getBoardId()).isEqualTo(responseDto.getId());
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

        List<Likes> likesList = new ArrayList<>();
        Likes likes = new Likes("id1", board1.getId(), 0L, new HashSet<>(), new HashSet<>());
        likes.addLikes(1L);
        likesList.add(likes);
        likesList.add(new Likes("id2", board2.getId(), 0L, new HashSet<>(), new HashSet<>()));

        when(boardRepository.findAll()).thenReturn(list);
        when(likesMongoRepository.findAllCommentIdIsNullAndOrderByBoardId()).thenReturn(likesList);

        // when
        List<BoardListResponseDto> findList = boardService.list();

        // then
        assertThat(findList.get(0).getTitle()).isEqualTo(board1.getTitle());
        assertThat(findList.get(0).getLikesCount()).isEqualTo(1);
        assertThat(findList.get(1).getTitle()).isNotEqualTo(board1.getTitle());
        assertThat(findList.get(1).getTitle()).isEqualTo(board2.getTitle());
    }

    @Test
    @DisplayName("좋아요/싫어요 테스트")
    void likeOrDislike()
    {
        // given
        Board board = new Board("title", "content", "email");
        Comment comment = new Comment(1L, "이메일", "내용", board, null, new ArrayList());
        Likes likesOnBoard = new Likes(board.getId(), 0L);
        Likes likesOnComment = new Likes(board.getId(), comment.getId());

        // mocking
        when(likesMongoRepository.findByBoardIdAndCommentIdIsNull(board.getId())).thenReturn(Optional.of(likesOnBoard));
        when(likesMongoRepository.findByCommentId(comment.getId())).thenReturn(Optional.of(likesOnComment));

        // when
        boardService.likeOrDislike(1L, board.getId(), 0L, true);
        boardService.likeOrDislike(1L, board.getId(), 1L, false);

        // then
        assertThat(likesOnBoard.getUserIdWhoLiked().size()).isEqualTo(1);
        assertThat(likesOnComment.getUserIdWhoDisliked().size()).isEqualTo(1);

        boardService.likeOrDislike(1L, board.getId(), 1L, false);
        assertThat(likesOnComment.getUserIdWhoDisliked().size()).isEqualTo(0);
    }
}
