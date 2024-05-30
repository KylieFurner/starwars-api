package com.kyliescode.starwars_api_challenge.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.kyliescode.starwars_api_challenge.exception.ResourceNotFound;
import com.kyliescode.starwars_api_challenge.model.Ship;
import com.kyliescode.starwars_api_challenge.repository.ShipRepository;

@Service
public class ShipService {
    private final ShipRepository shipRepository;
    private final ObjectMapper objectMapper;
    private static final String FILE_PATH = "C:\\Users\\Kylie\\Starwars API Challenge\\starwars-api-challenge\\jsonFiles\\starship_master.ndjson";

    public ShipService(ShipRepository shipRepository, ObjectMapper objectMapper) {
        this.shipRepository = shipRepository;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void importPlanets() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Ship ship = objectMapper.readValue(line, Ship.class);
                shipRepository.save(ship);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Ship> findAll() {
        return shipRepository.findAll();
    }

    public Ship getShip(Long id) {
        // retrieve a ship by finding its ID. Throw an Exception if the ship is not
        // found
        return shipRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Ship not found"));
    }

    public Ship saveShip(Ship ship) {
        // save a newly created ship to the ShipRepository
        return shipRepository.save(ship);
    }

    public void deleteShip(Long id) {
        shipRepository.deleteById(id);
        try {
            List<String> lines = Files.lines(Paths.get(FILE_PATH))
                    .filter(line -> {
                        Ship ship = mapToShip(line);
                        return ship == null || !ship.getId().equals(id);
                    })
                    .collect(Collectors.toList());

            Files.write(Paths.get(FILE_PATH), lines);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle file deletion error as needed
        }
    }

    private Ship mapToShip(String line) {
        try {
            // Use ObjectMapper to map JSON string to Ship object
            return objectMapper.readValue(line, Ship.class);
        } catch (MismatchedInputException e) {
            // Handle exception if there's a mismatch between the JSON content and the Ship
            // class
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // Handle other IO exceptions
            e.printStackTrace();
            return null;
        }
    }

    public Long getMaxShipId() {
        // find and return the max ship ID
            return shipRepository.findMaxId();
        }
}
