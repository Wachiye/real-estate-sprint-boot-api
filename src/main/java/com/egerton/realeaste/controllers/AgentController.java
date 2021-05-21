package com.egerton.realeaste.controllers;

import com.egerton.realeaste.dao.AgentRepo;
import com.egerton.realeaste.dao.HouseRepo;
import com.egerton.realeaste.dao.SubscriptionRepo;
import com.egerton.realeaste.models.Agent;
import com.egerton.realeaste.models.House;
import com.egerton.realeaste.models.Login;
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
@RequestMapping("/api/agents")
public class AgentController {
    @Autowired
    private AgentRepo agentRepo;
    @Autowired
    private HouseRepo houseRepo;
    @Autowired
    private SubscriptionRepo subscriptionRepo;

    //RETURNS A LIST OF AGENTS
    @GetMapping
    public List<Agent> findAllAgents(){
        return agentRepo.findAll();
    }

    //RETURNS A AGENT WITH GIVEN ID
    @GetMapping("/{id}")
    public ResponseEntity<?> findAgentById(@PathVariable(value = "id") long id) {
        //CHECK IF AGENT WITH ID EXISTS AND THROW ResourceNotFoundException if not found
        verifyAgent(id);
        // get  and return agent with given id
        Agent agent = agentRepo.getOne(id);
        return new ResponseEntity<> (agent, HttpStatus.OK);
    }
    //RETURNS ALL HOUSES OF A PARTICULAR AGENT GIVEN AGENT ID
    @GetMapping("/{id}/houses")
    public List<House> findHousesByAgentId(@PathVariable(value = "id") long id) {
        verifyAgent(id);
        return houseRepo.findAllByAgent(id);
    }

    //RETURNS A SINGLE HOUSE OF A GIVEN AGENT
    @GetMapping("/{id}/houses/{house_id}")
    public ResponseEntity<House> findHouseByAgentId(@PathVariable(value = "id") long id, @PathVariable(value = "house_id") long house_id) {
        verifyAgent(id);
        //CHECK IF house WITH ID EXISTS AND THROW ResourceNotFoundException if not found
        verifyHouse(house_id);
        return houseRepo.findHouseByAgentId(house_id, id);
    }

    //RETURNS A LIST OF SUBSCRIPTIONS OF A PARTICULAR AGENT
    @GetMapping("/{id}/subscriptions")
    public List<Subscription> findSubscriptionsByAgentId(@PathVariable(value = "id") long id) {
        verifyAgent(id);
        return subscriptionRepo.findAllByAgentId(id);
    }

    //RETURNS A SINGLE SUBSCRIPTION GIVEN THE ID OF A PARTICULAR AGENT
    @GetMapping("/{id}/subscriptions/{sub_id}")
    public ResponseEntity<Subscription> findSubscriptionByAgentId(@PathVariable(value = "id") long id, @PathVariable(value = "sub_id") long sub_id) {
        verifyAgent(id);
        return subscriptionRepo.findByAgentId(sub_id, id);
    }
    //SAVES A NEW AGENT TO THE DATABASE
    @PostMapping
    public Agent saveAgent(@Validated @RequestBody Agent agent) {
        //create a new instance of the Login class to access the encryptPassword function
        Login l = new Login();
        //encrypt the password before saving
        agent.setPassword( l.encryptPassword(agent.getPassword()));
        //save and return the agent
        return agentRepo.save(agent);
    }
    //UPDATES DETAILS OF A PARTICULAR AGENT
    @PutMapping
    public Agent updateAgent(@Validated @RequestBody Agent agent) {
        verifyAgent(agent.getId());
        return agentRepo.save(agent);
    }
    //DELETES A PARTICULAR AGENT
    @DeleteMapping("/{id}/delete")
    public void deleteAgent(@PathVariable(value = "id") long id){
        verifyAgent(id);
        agentRepo.deleteById(id);
    }
    //DELETES ALL AGENTS
    @DeleteMapping()
    public void deleteAllAgents(){
        agentRepo.deleteAll();
    }

    //Checks if agent with given id exists
    protected void verifyAgent(long agent_id) throws ResourceNotFoundException{
        Optional<Agent> agent = agentRepo.findById(agent_id);
        if(agent.isEmpty()){
            throw new ResourceNotFoundException("Agent with id " + agent_id + " not found");
        }
    }
    //checks if house exists
    private void verifyHouse(long house_id) throws ResourceNotFoundException {
        Optional<House> house = houseRepo.findById(house_id);
        if(house.isEmpty()){
            throw  new ResourceNotFoundException("House with id " + house_id + " not found");
        }
    }
}
