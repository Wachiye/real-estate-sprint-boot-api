package com.egerton.realeaste.dao;



import com.egerton.realeaste.models.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepo extends JpaRepository<Subscription, Long> {
    @Query(value = "SELECT * FROM subscriptions u WHERE u.agent_id =?", nativeQuery = true)
    List<Subscription> findAllByAgentId(long id);

    @Query(value = "SELECT * FROM subscription u WHERE DATEDIFF(NOW(),u.create_at) < 7", nativeQuery = true)
    List<Subscription> findRecent();

    @Query(value = "SELECT * FROM subscriptions u WHERE u.id = ? AND u.agent_id =?", nativeQuery = true)
    ResponseEntity<Subscription> findByAgentId(long sub_id, long id);
}