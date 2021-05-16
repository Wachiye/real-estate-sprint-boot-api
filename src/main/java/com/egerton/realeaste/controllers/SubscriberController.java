package com.egerton.realeaste.controllers;

import com.egerton.realeaste.dao.SubscriberRepo;
import com.egerton.realeaste.models.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/subscribers")
public class SubscriberController {
    @Autowired
    private SubscriberRepo subscriberRepo;

    @GetMapping
    public List<Subscriber> findAllSubscribers(){
        return subscriberRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscriber> findSubscriberById(@PathVariable(value = "id") long id) {
        Optional<Subscriber> subscriber = subscriberRepo.findById(id);
        if(subscriber.isPresent()){
            return ResponseEntity.ok().body(subscriber.get());
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

}
