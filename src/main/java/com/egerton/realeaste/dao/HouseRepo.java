package com.egerton.realeaste.dao;


import com.egerton.realeaste.models.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface HouseRepo extends JpaRepository<House, Long> {
    @Query(value = "SELECT * FROM houses u WHERE u.agent_id =?", nativeQuery = true)
    List<House> findAllByAgent(long id);

    @Query(value = "SELECT * FROM houses u WHERE u.id = ? AND u.agent_id =? ", nativeQuery = true)
    ResponseEntity<House> findHouseByAgentId(long house_id, long agent_id);

    @Query(value = "SELECT * FROM houses u WHERE DATEDIFF(NOW(),u.created_at) < 7", nativeQuery = true)
    List<House> findRecent();

    @Query(value = "SELECT * FROM houses u WHERE u.booked = 0", nativeQuery = true)
    List<House> findAllUnBooked();

    @Query(value = "UPDATE houses SET slots = houses.slots + ? WHERE id = ?", nativeQuery = true)
    void bookHouse(int slots, long id);

}
