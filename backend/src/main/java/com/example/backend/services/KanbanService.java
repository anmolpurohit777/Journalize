package com.example.backend.services;

import com.example.backend.entities.Kanban;
import com.example.backend.entities.User;
import com.example.backend.repositories.KanbanRepository;
import com.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KanbanService {

    @Autowired
    private KanbanRepository kanbanRepository;

    @Autowired
    private UserRepository userRepository;

    public Kanban createKanbanForUser(String userId, Kanban kanban) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();
        kanbanRepository.save(kanban);
        user.getKanbans().add(kanban);
        userRepository.save(user);
        return kanban;
    }

    public List<Kanban> getKanbansByUserId(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        return userOpt.get().getKanbans();
    }

    public Kanban getKanbanById(String userId, String kanbanId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();
        return user.getKanbans().stream()
                .filter(kanban -> kanban.getId().equals(kanbanId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Kanban task not found"));
    }

    public Kanban updateKanban(String userId, String kanbanId, Kanban updatedKanban) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();

        Kanban kanbanToUpdate = user.getKanbans().stream()
                .filter(kanban -> kanban.getId().equals(kanbanId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Kanban task not found"));

        kanbanToUpdate.setStatus(updatedKanban.getStatus());
        kanbanRepository.save(kanbanToUpdate);
        return kanbanToUpdate;
    }

    public void deleteKanban(String userId, String kanbanId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();

        Kanban kanbanToDelete = user.getKanbans().stream()
                .filter(kanban -> kanban.getId().equals(kanbanId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Kanban task not found"));

        user.getKanbans().remove(kanbanToDelete);
        kanbanRepository.delete(kanbanToDelete);
        userRepository.save(user);
    }

    public List<Kanban> getKanbansByStatus(String status) {
        return kanbanRepository.findByStatus(status);
    }
}
