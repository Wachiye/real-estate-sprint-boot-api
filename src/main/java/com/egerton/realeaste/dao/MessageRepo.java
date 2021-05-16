package com.egerton.realeaste.dao;


import com.egerton.realeaste.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepo extends JpaRepository<Message, Long> {
   @Query(value = "SELECT * FROM messages u WHERE DATEDIFF(NOW(),u.created_at) < 7", nativeQuery = true)
    List<Message> findRecent();

    @Query(value = "SELECT * FROM messages u WHERE u.status = 0", nativeQuery = true)
    List<Message> findAllUnread();

    @Query(value = "UPDATE messages SET read = 1 WHERE id = ?", nativeQuery = true)
    ResponseEntity<Message> markAsRead(long id);
}
