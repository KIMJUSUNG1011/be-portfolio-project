package com.jw.boardservice.board;

import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoardRepositoryForMongo extends MongoRepository<BoardForLikeOrDislike, Long>
{
}
