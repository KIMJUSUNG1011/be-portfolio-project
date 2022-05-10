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

    public Long write(BoardWriteRequestDto requestDto)
    {
        return null;
    }

    public Long edit(Long id, BoardEditRequestDto requestDto)
    {
        return null;
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
