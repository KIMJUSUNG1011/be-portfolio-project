package com.jw.boardservice.board;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryForMongo extends MongoRepository<Likes, String>
{
    Optional<Likes> findByCommentId(Long commentId);
    Optional<Likes> findByBoardId(Long boardId);
    List<Likes> findAllCommentIdIsNull();
}
