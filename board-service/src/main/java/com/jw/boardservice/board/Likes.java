package com.jw.boardservice.board;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.*;

@Document
@Getter
public class Likes
{
    @Id
    private String id;

    private Long boardId;
    private Long commentId;

    private Set<Long> userIdWhoLiked = new HashSet<>();
    private Set<Long> userIdWhoDisliked = new HashSet<>();

    public Likes(Long boardId, Long commentId)
    {
        this.boardId = boardId;
        this.commentId = commentId;
    }

    public void addLikes(Long userId)
    {
        userIdWhoLiked.add(userId);
    }

    public void removeLikes(Long userId)
    {
        userIdWhoLiked.remove(userId);
    }

    public void addDislikes(Long userId)
    {
        userIdWhoDisliked.add(userId);
    }

    public void removeDisliked(Long userId)
    {
        userIdWhoDisliked.remove(userId);
    }
}
