package com.example.backend.repositories;

import com.example.backend.entities.Kanban;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KanbanRepository extends MongoRepository<Kanban, String> {
    List<Kanban> findByStatus(String status);
}
