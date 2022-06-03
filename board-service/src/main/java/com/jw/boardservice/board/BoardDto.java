package com.jw.boardservice.board;

import com.jw.boardservice.file.FileDto.*;
import com.jw.boardservice.likes.LikesDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

import static com.jw.boardservice.comment.CommentDto.*;

public class BoardDto
{
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardWriteRequestDto
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
    public static class BoardEditRequestDto
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

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardReadResponseDto
    {
        private Long id;
        private String title;
        private String content;
        private String email;
        private int count;
        private LocalDateTime registerDate;
        private List<FileReadResponseDto> files;
        private List<CommentResponseDto> comments;
        private LikesDto likes;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardListResponseDto
    {
        private Long id;
        private String title;
        private String email;
        private int count;
        private int commentCount;
        private LocalDateTime registerDate;
        private int likesCount;
        private int dislikesCount;
    }
}
