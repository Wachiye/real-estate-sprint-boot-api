package com.egerton.realeaste.controllers;

import com.egerton.realeaste.dao.AgentRepo;
import com.egerton.realeaste.dao.LoginRepo;
import com.egerton.realeaste.dao.SubscriberRepo;
import com.egerton.realeaste.models.Agent;
import com.egerton.realeaste.models.Login;
import com.egerton.realeaste.models.LoginRequest;
import com.egerton.realeaste.models.Subscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private LoginRepo loginRepo;

    @Autowired
    private AgentRepo agentRepo;

    @Autowired
    private SubscriberRepo subscriberRepo;

    @PostMapping("/login")
    public ResponseEntity<Login> login(@RequestBody LoginRequest loginRequest){
        Optional<Agent> agent = agentRepo.findByEmail(loginRequest.getEmail());

        Agent a = agent.get();
        Login l = new Login();

        boolean passMatch = l.comparePassword(a.getPassword(),loginRequest.getPassword());

        if(agent.isPresent() && passMatch) {
            //generate a jwt
            l.setEmail(loginRequest.getEmail());
            l.set_active(true);
            //l.setToken(token);
            return  ResponseEntity.ok().body(loginRepo.save(l));
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/logout/{token}")
    Boolean logout(@PathVariable(value = "token") String token){
        return loginRepo.logout(token);
    }

    @GetMapping("/checkemail/{email}")
    Boolean checkEmail(@PathVariable(value = "email") String email){
        if(loginRepo.checkAgentEmail(email) || loginRepo.checkSubscriberEmail(email))
            return true;
        else
            return false;
    }

    @GetMapping("/active")
    List<Login> findAllActive(){
        return loginRepo.findAllActive();
    }

    @GetMapping("/unsubscribe/{email}")
    public ResponseEntity<Subscriber> unsubscribe(@PathVariable(value = "email") String email) {
        Optional<Subscriber> subscriber = subscriberRepo.findByEmail(email);
        if(subscriber.isPresent()){
            ResponseEntity<Subscriber> sub = subscriberRepo.unsubscribe(email);
            return ResponseEntity.ok().body(sub.getBody());
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/subscribe")
    public Subscriber saveSubscriber(@Validated @RequestBody Subscriber subscriber) {
        return subscriberRepo.save(subscriber);
    }

}
