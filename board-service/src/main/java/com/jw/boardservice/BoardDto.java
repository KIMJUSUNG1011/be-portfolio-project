package com.jw.boardservice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BoardDto
{
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class BoardWriteRequestDto
    {

        private String title;
        private String content;

        public Board toEntity(String email)
        {
            return Board.builder()
                    .title(title)
                    .content(content)
                    .email(email)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class BoardEditRequestDto
    {
        private String title;
        private String content;

        public Board toEntity()
        {
            return Board.builder()
                    .title(title)
                    .content(content)
                    .build();
        }
    }
}
