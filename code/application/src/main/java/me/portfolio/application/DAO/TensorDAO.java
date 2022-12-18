package me.portfolio.application.DAO;

import me.portfolio.library.entity.Tensor;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TensorDAO extends MongoRepository<Tensor, String> {
}
