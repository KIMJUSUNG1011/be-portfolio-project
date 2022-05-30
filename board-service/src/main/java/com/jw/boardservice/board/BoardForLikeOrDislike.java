package com.jw.boardservice.board;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.util.List;

@Document("event")
@Data
public class BoardForLikeOrDislike
{
    @Id
    private Long id;
    private List<Long> usersWhoLiked;
    private List<Long> usersWhoDisliked;
}
