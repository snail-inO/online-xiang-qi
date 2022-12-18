package me.portfolio.application.DAO.test;

import me.portfolio.library.entity.test.Metadata;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MetadataDAO extends MongoRepository<Metadata, String> {
}
