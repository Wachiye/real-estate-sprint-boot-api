package com.egerton.realeaste.dao;
import com.egerton.realeaste.models.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AgentRepo extends JpaRepository<Agent, Long> {

    @Query(value = "SELECT * FROM agents u WHERE u.email = ?", nativeQuery = true)
    Optional<Agent> findByEmail( String email);

}
