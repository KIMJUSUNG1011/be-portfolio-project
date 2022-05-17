package com.jw.boardservice.board;

import com.jw.boardservice.file.FileDto.*;
import com.jw.boardservice.file.FileEntity;
import com.jw.boardservice.file.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.jw.boardservice.board.BoardDto.*;
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
    public List<Board> list()
    {
        return boardRepository.findAll();
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
        List<FileEntity> files = board.getFiles();
        List<FileReadResponseDto> responseFiles = new ArrayList<>();

        String location = System.getProperty("java.io.tmpdir");

        for (FileEntity f : files) {
            Path path = Paths.get(location, f.getPath());
            responseFiles.add(new FileReadResponseDto(
                    f.getName(), path.toString(), f.getSize()
            ));
        }

        BoardReadResponseDto readResponseDto = new BoardReadResponseDto(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getEmail(),
                board.getCount(),
                board.getRegisterDate(),
                responseFiles
        );

        return readResponseDto;
    }
}
