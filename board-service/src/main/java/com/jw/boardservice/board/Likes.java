package com.jw.boardservice.board;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;

@Document
@Data
public class Likes
{
    @Id
    private String id;

    private Long boardId;
    private Long commentId;

    private List<Long> UserIdWhoLiked = new ArrayList<>();
    private List<Long> userIdWhoDisliked = new ArrayList<>();
}
