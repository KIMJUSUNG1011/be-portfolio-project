package com.jw.boardservice.likes;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LikesMongoRepository extends MongoRepository<Likes, String>
{
    Optional<Likes> findByCommentId(Long commentId);
    Optional<Likes> findByBoardIdAndCommentIdIsNull(Long boardId);
    Optional<Likes> findByBoardIdAndCommentId(Long boardId, Long commentId);
    List<Likes> findAllByBoardIdAndCommentIdIsNotNull(Long boardId);
    List<Likes> findAllByBoardIdOrderByCommentId(Long boardId);
    List<Likes> findAllCommentIdIsNullAndOrderByBoardId();
}
