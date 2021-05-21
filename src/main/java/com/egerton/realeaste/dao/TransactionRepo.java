package com.egerton.realeaste.dao;

import com.egerton.realeaste.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {
    @Query(value = "SELECT * FROM transactions WHERE checkout_requestid = ? AND accepted = 0", nativeQuery = true)
    Optional<Transaction> getTransactionsByCheckoutRequestID( String checkout_id);
}
