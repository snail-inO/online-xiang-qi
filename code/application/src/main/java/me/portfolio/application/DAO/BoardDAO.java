package me.portfolio.application.DAO;

import me.portfolio.library.entity.Board;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

@RepositoryRestResource(collectionResourceRel = "board", path = "board")
public interface BoardDAO extends MongoRepository<Board, String> {
}
