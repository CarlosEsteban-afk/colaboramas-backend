package com.agora.message.service;

import com.agora.message.model.UserInteraction;
import com.agora.message.repository.UserInteractionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInteractionService {

    private final UserInteractionRepository repo;

    public UserInteractionService(UserInteractionRepository repo) {
        this.repo = repo;
    }


    public void registerContact(Long fromUserId, Long toUserId) {
        if (!repo.existsByFromUserIdAndToUserId(fromUserId, toUserId)) {
            UserInteraction i = new UserInteraction();
            i.setFromUserId(fromUserId);
            i.setToUserId(toUserId);
            i.setType("contact");
            repo.save(i);
        }
    }


    public List<Long> getUsersContacted(Long fromUserId) {
        return repo.findByFromUserId(fromUserId)
                .stream()
                .map(UserInteraction::getToUserId)
                .toList();
    }
}
