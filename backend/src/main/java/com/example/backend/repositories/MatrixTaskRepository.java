package com.example.backend.repositories;

import com.example.backend.entities.MatrixTask;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatrixTaskRepository extends MongoRepository<MatrixTask, String> {
}