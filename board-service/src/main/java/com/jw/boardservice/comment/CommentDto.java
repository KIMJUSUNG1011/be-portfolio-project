package com.jw.boardservice.comment;

import com.jw.boardservice.board.Board;
import com.jw.boardservice.likes.LikesDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class CommentDto
{
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentWriteRequestDto {
        private String content;

        public Comment toEntity(String email, Board board) {
            return Comment.builder()
                    .content(content)
                    .email(email)
                    .board(board)
                    .build();
        }

        public Comment toEntity(String email, Board board, Comment parent) {
            return Comment.builder()
                    .content(content)
                    .email(email)
                    .board(board)
                    .parent(parent)
                    .build();
        }
    }

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
    public static class CommentResponseDto {
        private Long id;
        private String email;
        private String content;
        private Long parentId;
        private LocalDateTime registerDate;
        private LikesDto likes;
    }
}
