package me.portfolio.application.DAO;

import me.portfolio.library.entity.Board;
import org.springframework.data.mongodb.repository.MongoRepository;



public interface BoardDAO extends MongoRepository<Board, String> {

}
