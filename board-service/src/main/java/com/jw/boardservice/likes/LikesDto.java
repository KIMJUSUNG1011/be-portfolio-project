package com.jw.boardservice.likes;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class LikesDto
{
    private String id;

    private Long boardId;
    private Long commentId;

    private Set<Long> userIdWhoLiked;
    private Set<Long> userIdWhoDisliked;
}
