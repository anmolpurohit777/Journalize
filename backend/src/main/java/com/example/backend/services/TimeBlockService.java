package com.example.backend.services;

import com.example.backend.entities.TimeBlock;
import com.example.backend.entities.User;
import com.example.backend.repositories.TimeBlockRepository;
import com.example.backend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TimeBlockService {

    @Autowired
    private TimeBlockRepository timeBlockRepository;

    @Autowired
    private UserRepository userRepository;

    public TimeBlock createTimeBlockForUser(String userId, TimeBlock timeBlock) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();
        timeBlockRepository.save(timeBlock);
        if (user.getTimeBlocks() == null) {
            user.setTimeBlocks(new java.util.ArrayList<>());
        }
        user.getTimeBlocks().add(timeBlock);
        userRepository.save(user);
        return timeBlock;
    }

    public List<TimeBlock> getTimeBlocksByUserId(String userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        return userOpt.get().getTimeBlocks();
    }

    public TimeBlock getTimeBlockById(String userId, String blockId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();
        return user.getTimeBlocks().stream()
                .filter(block -> block.getId().equals(blockId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("TimeBlock not found"));
    }

    public TimeBlock updateTimeBlock(String userId, String blockId, TimeBlock updatedBlock) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();

        TimeBlock blockToUpdate = user.getTimeBlocks().stream()
                .filter(block -> block.getId().equals(blockId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("TimeBlock not found"));

        blockToUpdate.setStartTime(updatedBlock.getStartTime());
        blockToUpdate.setEndTime(updatedBlock.getEndTime());
        blockToUpdate.setDescription(updatedBlock.getDescription());
        timeBlockRepository.save(blockToUpdate);
        return blockToUpdate;
    }

    public void deleteTimeBlock(String userId, String blockId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();

        TimeBlock blockToDelete = user.getTimeBlocks().stream()
                .filter(block -> block.getId().equals(blockId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("TimeBlock not found"));

        user.getTimeBlocks().remove(blockToDelete);
        timeBlockRepository.delete(blockToDelete);
        userRepository.save(user);
    }
}
