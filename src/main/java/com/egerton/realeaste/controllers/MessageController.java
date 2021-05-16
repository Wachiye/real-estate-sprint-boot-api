package com.egerton.realeaste.controllers;

import com.egerton.realeaste.dao.MessageRepo;
import com.egerton.realeaste.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    private MessageRepo messageRepo;

    @GetMapping
    public List<Message> findAllAgents(){
        return messageRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> findAgentById(@PathVariable(value = "id") long id) {
        Optional<Message> Message = messageRepo.findById(id);
        if(Message.isPresent()){
            return ResponseEntity.ok().body(Message.get());
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/unread")
    public List<Message> findAllUnread(){
        return messageRepo.findAllUnread();
    }

    @GetMapping("/recent")
    public List<Message> findRecent(){
        return messageRepo.findRecent();
    }

    @GetMapping("/{id}/read")
    public ResponseEntity<Message> markAsRead(@PathVariable(value = "id") long id) {
        Optional<Message> m = messageRepo.findById(id);
        if (m.isPresent()) {
            return messageRepo.markAsRead(id);
        } else {
            return null;
        }
    }

    @PostMapping
    public Message saveMessage(@Validated @RequestBody Message message) {
        return messageRepo.save(message);
    }


    @DeleteMapping("/{id}/delete")
    public void deleteMessage(@PathVariable(value = "id") long id){
        messageRepo.deleteById(id);
    }

    @DeleteMapping()
    public void deleteAllMessages(){
        messageRepo.deleteAll();
    }
}

