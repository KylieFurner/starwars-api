package com.kyliescode.starwars_api_challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kyliescode.starwars_api_challenge.model.Planet;

@Repository
public interface PlanetRepository extends JpaRepository<Planet, Long> {
    /**
     * Custom query to find the maximum ID of Planet entities in the database.
     *
     * @return the maximum ID of Planet entities, or null if no entities exist
     */
    @Query("SELECT MAX(p.id) FROM Planet p")
    Long findMaxId();
}
