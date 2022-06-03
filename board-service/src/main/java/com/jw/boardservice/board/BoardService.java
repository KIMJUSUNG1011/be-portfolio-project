package com.jw.boardservice.board;

import com.jw.boardservice.board.BoardDto.BoardEditRequestDto;
import com.jw.boardservice.board.BoardDto.BoardListResponseDto;
import com.jw.boardservice.board.BoardDto.BoardReadResponseDto;
import com.jw.boardservice.board.BoardDto.BoardWriteRequestDto;
import com.jw.boardservice.file.FileDto.FileReadResponseDto;
import com.jw.boardservice.file.FileDto.FileWriteRequestDto;
import com.jw.boardservice.file.FileEntity;
import com.jw.boardservice.file.FileRepository;
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

@RequiredArgsConstructor
@Service
@Transactional
public class BoardService
{
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;
    private final BoardRepositoryForMongo boardMongoRepository;

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
        if(board == null)
            return false;

        if(!email.equals(board.getEmail()))
            return false;

        boardRepository.delete(board);

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

        return responseDto;
    }

    @Transactional(readOnly = true)
    public List<BoardListResponseDto> list()
    {
        List<Board> boardList = boardRepository.findAll();
        List<BoardListResponseDto> responseDtoList = new ArrayList<>();

        for(Board board : boardList) {
            BoardListResponseDto responseDto = new ModelMapper().map(board, BoardListResponseDto.class);
            responseDto.setCommentCount(board.getComments().size());
            responseDtoList.add(responseDto);
        }

        return responseDtoList;
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
