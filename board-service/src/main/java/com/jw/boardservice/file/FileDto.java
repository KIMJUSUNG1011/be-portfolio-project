package com.jw.boardservice.file;

import com.jw.boardservice.board.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class FileDto
{
    @Getter
    @AllArgsConstructor
    public static class FileWriteRequestDto
    {
        private String name;
        private String path;
        private Long size;

        public FileEntity toEntity(Board board)
        {
            return FileEntity.builder()
                    .name(name)
                    .board(board)
                    .path(path)
                    .size(size)
                    .build();
        }
    }
}
