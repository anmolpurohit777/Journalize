package com.example.backend.repositories;

import com.example.backend.entities.TimeBlock;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeBlockRepository extends MongoRepository<TimeBlock, String> {

}
