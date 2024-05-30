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
import com.kyliescode.starwars_api_challenge.model.Planet;
import com.kyliescode.starwars_api_challenge.repository.PlanetRepository;

@Service
public class PlanetService {
    private final PlanetRepository planetRepository;
    private final ObjectMapper objectMapper;
    private static final String FILE_PATH = "C:\\Users\\Kylie\\Starwars API Challenge\\starwars-api-challenge\\jsonFiles\\galaxy_planets.ndjson";

    public PlanetService(PlanetRepository planetRepository, ObjectMapper objectMapper) {
        this.planetRepository = planetRepository;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void importPlanets() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Planet planet = objectMapper.readValue(line, Planet.class);
                planetRepository.save(planet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Planet> findAll() {
        return planetRepository.findAll();
    }

    public Planet getPlanet(Long id) {
        // retrieve a planet by finding its ID. Throw an Exception if the planet is not
        // found
        return planetRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Planet not found"));
    }

    public Planet savePlanet(Planet planet) {
        // save a newly created planet to the planetRepository
        return planetRepository.save(planet);
    }

    public void deletePlanet(Long id) {
        planetRepository.deleteById(id);
        try {
            List<String> lines = Files.lines(Paths.get(FILE_PATH))
                    .filter(line -> {
                        Planet planet = mapToPlanet(line);
                        return planet == null || !planet.getId().equals(id);
                    })
                    .collect(Collectors.toList());

            Files.write(Paths.get(FILE_PATH), lines);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle file deletion error as needed
        }
    }

    private Planet mapToPlanet(String line) {
        try {
            // Use ObjectMapper to map JSON string to planet object
            return objectMapper.readValue(line, Planet.class);
        } catch (MismatchedInputException e) {
            // Handle exception if there's a mismatch between the JSON content and the
            // planet class
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // Handle other IO exceptions
            e.printStackTrace();
            return null;
        }
    }

    public Long getMaxPlanetId() {
        // find and return the max planet ID
        return planetRepository.findMaxId();
    }
}
