package com.example.backend.repositories;

import com.example.backend.entities.CountdownGoal;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountdownGoalRepository extends MongoRepository<CountdownGoal, String> {
}
