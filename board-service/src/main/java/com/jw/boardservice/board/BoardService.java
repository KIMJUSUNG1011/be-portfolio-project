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
import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class BoardService
{
    private final BoardRepository boardRepository;
    private final FileRepository fileRepository;

    public Long write(String email, BoardWriteRequestDto requestDto, List<MultipartFile> files) throws IOException
    {
        Board board = boardRepository.save(requestDto.toEntity(email));

        System.out.println(
                "files : " + board.getFiles()
        );

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
    public Board read(Long id)
    {
        return boardRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<Board> list()
    {
        return boardRepository.findAll();
    }
}
