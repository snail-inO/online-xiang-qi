package me.portfolio.application.DAO;

import me.portfolio.library.entity.Piece;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "piece", path= "piece")
public interface PieceDAO extends MongoRepository<Piece, String> {
}
