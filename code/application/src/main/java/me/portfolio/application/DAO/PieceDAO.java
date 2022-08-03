package me.portfolio.application.DAO;

import me.portfolio.library.entity.Piece;
import org.springframework.data.mongodb.repository.MongoRepository;



public interface PieceDAO extends MongoRepository<Piece, String> {
}
