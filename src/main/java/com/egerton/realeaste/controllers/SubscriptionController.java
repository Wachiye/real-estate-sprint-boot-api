package com.egerton.realeaste.controllers;

import com.egerton.realeaste.dao.SubscriptionRepo;
import com.egerton.realeaste.models.House;
import com.egerton.realeaste.models.Subscription;
import com.egerton.realeaste.utils.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    @Autowired
    private SubscriptionRepo subscriptionRepo;

    @GetMapping
    public List<Subscription> findAllSubscriptions(){
        return subscriptionRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> findSubscriptionById(@PathVariable(value = "id") long id) {
        verifySubscription(id);
        Subscription subscription = subscriptionRepo.getOne(id);
        return new ResponseEntity<>(subscription, HttpStatus.OK);
    }

    @GetMapping("/recent")
    public List<Subscription> findRecent() {
        return subscriptionRepo.findRecent();
    }

    @PostMapping
    public Subscription saveSubscription(@Validated @RequestBody Subscription subscription) {
        return subscriptionRepo.save(subscription);
    }

    @PutMapping
    public Subscription updateSubscription(@Validated @RequestBody Subscription subscription) {
        verifySubscription(subscription.getId());
        return subscriptionRepo.save(subscription);
    }

    @DeleteMapping("/{id}/delete")
    public void deleteSubscription(@PathVariable(value = "id") long id){
        verifySubscription(id);
        subscriptionRepo.deleteById(id);
    }

    @DeleteMapping()
    public void deleteAllSubscriptions(){
        subscriptionRepo.deleteAll();
    }

    //checks if house exists
    private void verifySubscription(long subscription_id) throws ResourceNotFoundException {
        Optional<Subscription> subscription = subscriptionRepo.findById(subscription_id);
        if(subscription.isEmpty()){
            throw  new ResourceNotFoundException("Subscription with id " + subscription_id + " not found");
        }
    }
}
