package com.jw.boardservice.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CommentDto
{
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentEditRequestDto
    {
        public String content;

        public Comment toEntity()
        {
            return Comment.builder()
                    .content(content)
                    .build();
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentResponseDto
    {
        private Long id;
        private String email;
        private String content;
        private Long parentId;
    }
}
