package com.jw.boardservice.board;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface BoardRepositoryForMongo extends MongoRepository<Likes, String>
{
}
