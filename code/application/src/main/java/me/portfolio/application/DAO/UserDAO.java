package me.portfolio.application.DAO;

import me.portfolio.library.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface UserDAO extends MongoRepository<User, String> {

}
