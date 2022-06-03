package com.jw.boardservice.board;

import com.jw.boardservice.board.BoardDto.BoardEditRequestDto;
import com.jw.boardservice.board.BoardDto.BoardListResponseDto;
import com.jw.boardservice.board.BoardDto.BoardReadResponseDto;
import com.jw.boardservice.board.BoardDto.BoardWriteRequestDto;
import com.jw.boardservice.file.FileDto.FileReadResponseDto;
import com.jw.boardservice.file.FileDto.FileWriteRequestDto;
import com.jw.boardservice.file.FileEntity;
import com.jw.boardservice.file.FileRepository;
import com.jw.boardservice.likes.Likes;
import com.jw.boardservice.likes.LikesDto;
import com.jw.boardservice.likes.LikesMongoRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.jw.boardservice.comment.CommentDto.*;

@RequiredArgsConstructor
@Service
@Transactional
public class BoardService
{
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;
    private final LikesMongoRepository likesMongoRepository;

    public Long write(String email, BoardWriteRequestDto requestDto, List<MultipartFile> files) throws Exception
    {
        Board board = boardRepository.save(requestDto.toEntity(email));

        uploadFiles(board, files);

        return board.getId();
    }

    public boolean edit(String email, Long id, BoardEditRequestDto requestDto)
    {
        Board board = boardRepository.findById(id).orElse(null);

        if (board == null) {
            return false;
        }

        if (!email.equals(board.getEmail())) {
            return false;
        }

        board.update(requestDto.toEntity());
        return true;
    }

    public Boolean delete(String email, Long id)
    {
        Board board = boardRepository.findById(id).orElse(null);
        if (board == null)
            return false;

        if (!email.equals(board.getEmail()))
            return false;

        Likes boardLikes = likesMongoRepository.findByBoardIdAndCommentIdIsNull(id).orElse(null);
        if (boardLikes == null)
            return false;

        List<Likes> commentLikes = likesMongoRepository.findAllByBoardIdAndCommentIdIsNotNull(id);

        boardRepository.delete(board);

        likesMongoRepository.delete(boardLikes);

        for (Likes likes : commentLikes)
        {
            likesMongoRepository.delete(likes);
        }

        return true;
    }

    public BoardReadResponseDto read(Cookie cookie, Long id)
    {
        Board board = boardRepository.findById(id).orElse(null);
        if(board == null)
            return null;

        if(cookie != null)
            board.increaseViewCount();

        BoardReadResponseDto responseDto = new ModelMapper().map(board, BoardReadResponseDto.class);
        setPathToFiles(responseDto);

        List<Likes> likesList = likesMongoRepository.findAllByBoardIdOrderByCommentId(id);
        LikesDto likesDto = new ModelMapper().map(likesList.get(0), LikesDto.class);
        responseDto.setLikes(likesDto);

        for(int i=1; i<likesList.size(); i++)
        {
            CommentResponseDto commentResponseDto = responseDto.getComments().get(i);
            commentResponseDto.setLikes(new ModelMapper().map(likesList.get(i), LikesDto.class));
        }

        return responseDto;
    }

    @Transactional(readOnly = true)
    public List<BoardListResponseDto> list()
    {
        List<Board> boardList = boardRepository.findAll();
        List<Likes> likesList = likesMongoRepository.findAllCommentIdIsNullAndOrderByBoardId();
        List<BoardListResponseDto> responseDtoList = new ArrayList<>();

        for (int i = 0; i < boardList.size(); i++)
        {
            Board board = boardList.get(i);
            Likes likes = likesList.get(i);
            BoardListResponseDto responseDto = new ModelMapper().map(board, BoardListResponseDto.class);
            responseDto.setCommentCount(board.getComments().size());
            responseDto.setLikesCount(likes.getUserIdWhoLiked().size());
            responseDto.setDislikesCount(likes.getUserIdWhoDisliked().size());
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
    }

    public boolean likeOrDislike(Long userId, Long boardId, Long commentId, Boolean isLike)
    {
        Likes likes = (commentId == 0L) ? likesMongoRepository.findByBoardIdAndCommentIdIsNull(boardId).orElse(null)
                                        : likesMongoRepository.findByCommentId(commentId).orElse(null);
        if(likes == null)
            likes = (commentId == 0L) ? new Likes(boardId, 0L) : new Likes(boardId, commentId);

        boolean isAlreadyLiked = likes.getUserIdWhoLiked().contains(userId);
        boolean isAlreadyDisliked = likes.getUserIdWhoDisliked().contains(userId);
        if(!isAlreadyLiked && !isAlreadyDisliked)
        {
            if(isLike)
                likes.addLikes(userId);
            else
                likes.addDislikes(userId);
        }
        else if(isAlreadyLiked)
        {
            if(isLike)
                likes.removeLikes(userId);
            else
                return false;
        }
        else if(isAlreadyDisliked)
        {
            if(!isLike)
                likes.removeDisliked(userId);
            else
                return false;
        }

        return true;
    }

    private void uploadFiles(Board board, List<MultipartFile> files) throws Exception
    {
        for (MultipartFile file : files)
        {
            FileWriteRequestDto fileWriteRequestDto = new FileWriteRequestDto(
                    System.nanoTime() + file.getOriginalFilename(),
                    file.getOriginalFilename(),
                    file.getSize()
            );

            file.transferTo(new File(fileWriteRequestDto.getName()));

            FileEntity fileEntity = fileWriteRequestDto.toEntity(board);

            board.addFile(fileEntity);

            fileRepository.save(fileEntity);
        }
    }

    private void setPathToFiles(BoardReadResponseDto responseDto)
    {
        String location = System.getProperty("java.io.tmpdir");

        for(FileReadResponseDto dto : responseDto.getFiles())
        {
            Path path = Paths.get(location, dto.getPath());
            dto.setPath(path.toString());
        }
    }
}
