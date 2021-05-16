package com.egerton.realeaste.controllers;

import com.egerton.realeaste.dao.BookingRepo;
import com.egerton.realeaste.models.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    private BookingRepo bookingRepo;

    @GetMapping
    public List<Booking> findAllBookings(){
        return bookingRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Booking> findBookingById(@PathVariable(value = "id") long id){
        Optional<Booking> booking = bookingRepo.findById(id);
        if( booking.isPresent()){
            return ResponseEntity.ok().body(booking.get());
        }
        else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/house/{id}")
    public List<Booking> findAllByHouseId( @PathVariable( value = "id") long id){
        return bookingRepo.findAllByHouseId(id);
    }

    @PostMapping
    public Booking book(@Validated @RequestBody Booking booking){
        return bookingRepo.save(booking);
    }
}
