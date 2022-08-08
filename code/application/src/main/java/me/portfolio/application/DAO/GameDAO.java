package me.portfolio.application.DAO;

import me.portfolio.library.entity.Game;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface GameDAO extends MongoRepository<Game, String> {
}
