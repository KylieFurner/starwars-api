package com.kyliescode.starwars_api_challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kyliescode.starwars_api_challenge.model.Ship;

@Repository
public interface ShipRepository extends JpaRepository<Ship, Long> {
    /**
     * Custom query to find the maximum ID of Ship entities in the database.
     *
     * @return the maximum ID of Ship entities, or null if no entities exist
     */
    @Query("SELECT MAX(s.id) FROM Ship s")
    Long findMaxId();
}
