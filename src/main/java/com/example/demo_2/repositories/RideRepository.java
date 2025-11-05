package com.example.demo_2.repositories;

import com.example.demo_2.entities.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findByUserId(Long userId);
    List<Ride> findByUserIdAndStatus(Long userId, String status);
    List<Ride> findByStatus(String status);
}