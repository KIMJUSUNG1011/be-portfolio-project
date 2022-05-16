package com.jw.boardservice;

import lombok.Getter;

public class FileDto
{
    @Getter
    public static class FileWriteRequestDto
    {
        private String name;
        private String path;
        private Long size;
    }
}
