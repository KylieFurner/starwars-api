package com.kyliescode.starwars_api_challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kyliescode.starwars_api_challenge.model.Character;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {

    /**
     * Custom query to find the maximum ID of Character entities in the database.
     *
     * @return the maximum ID of Character entities, or null if no entities exist
     */
    @Query("SELECT MAX(c.id) FROM Character c")
    Long findMaxId();
}