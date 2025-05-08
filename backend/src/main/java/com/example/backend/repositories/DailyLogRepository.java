package com.example.backend.repositories;

import com.example.backend.entities.DailyLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyLogRepository extends MongoRepository<DailyLog, String> {
}
