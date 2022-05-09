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

    public Boolean remove(Long id)
    {
        return false;
    }

    @Transactional(readOnly = true)
    public Board read(Long id)
    {
        return null;
    }

    @Transactional(readOnly = true)
    public List<Board> list()
    {
        return null;
    }
}
