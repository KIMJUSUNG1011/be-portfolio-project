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

    public Long write(String email, BoardWriteRequestDto requestDto, List<MultipartFile> files) throws Exception
    {
        Board board = boardRepository.save(requestDto.toEntity(email));

        uploadFiles(board, files);

        return board.getId();
    }

    public boolean edit(Long id, String email, BoardEditRequestDto requestDto)
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

    public Boolean delete(Long id)
    {
        Board board = boardRepository.findById(id).orElse(null);
        if(board == null)
            return false;

        boardRepository.delete(board);

        return true;
    }

    @Transactional(readOnly = true)
    public BoardReadResponseDto read(Long id)
    {
        Board board = boardRepository.findById(id).orElse(null);

        BoardReadResponseDto readResponseDto = attachFilesToBoard(board);

        return readResponseDto;
    }

    @Transactional(readOnly = true)
    public List<BoardListResponseDto> list()
    {
        List<Board> boardList = boardRepository.findAll();
        List<BoardListResponseDto> responseDtoList = new ArrayList<>();
        for(Board board : boardList)
            responseDtoList.add(new ModelMapper().map(board, BoardListResponseDto.class));

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

    private BoardReadResponseDto attachFilesToBoard(Board board)
    {
        String location = System.getProperty("java.io.tmpdir");

        BoardReadResponseDto responseDto = new ModelMapper().map(board, BoardReadResponseDto.class);
        for(FileReadResponseDto dto : responseDto.getFiles())
        {
            Path path = Paths.get(location, dto.getPath());
            dto.setPath(path.toString());
        }

        return responseDto;
    }
}
