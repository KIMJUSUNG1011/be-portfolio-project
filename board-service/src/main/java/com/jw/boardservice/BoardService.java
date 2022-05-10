package com.jw.boardservice;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.jw.boardservice.BoardDto.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class BoardService
{
    private final BoardRepository boardRepository;

    public Long write(String email, BoardWriteRequestDto requestDto)
    {
        Board board = boardRepository.save(requestDto.toEntity(email));
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

        board.update(board);
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
