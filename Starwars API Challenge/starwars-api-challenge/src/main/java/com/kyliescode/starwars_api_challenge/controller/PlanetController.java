package com.kyliescode.starwars_api_challenge.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kyliescode.starwars_api_challenge.model.Planet;
import com.kyliescode.starwars_api_challenge.service.PlanetService;

@Controller
public class PlanetController {
    // controller class for all planets
    private final PlanetService planetService;
    private static final String FILE_PATH = "C:\\Users\\Kylie\\Starwars API Challenge\\starwars-api-challenge\\jsonFiles\\galaxy_planets.ndjson";

    public PlanetController(PlanetService planetService) {
        this.planetService = planetService;
    }

    @GetMapping("/allPlanets")
    public String getAllPlanets(Model model) {
        // find all the planets and create a table of all of them
        List<Planet> planets = planetService.findAll();
        model.addAttribute("planets", planets);
        return "allPlanets";
    }

    @GetMapping("/searchPlanet")
    public String searchPlanetPage() {
        // initialize the search view for planets
        return "searchPlanet";
    }

    @PutMapping("/planet/{id}")
    @ResponseBody
    public ResponseEntity<Planet> updatePlanet(@PathVariable Long id, @RequestBody Planet updatedPlanet) {
        // after searching a planet find it by its index to update it
        Planet planet = planetService.getPlanet(id);
        // check for errors such as no input and invlaid input
        if (planet != null) {
            planet.setName(updatedPlanet.getName());
            planet.setClimate(updatedPlanet.getClimate());
            planet.setPopulation(updatedPlanet.getPopulation());

            Planet savedPlanet = planetService.savePlanet(planet);
            return new ResponseEntity<>(savedPlanet, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/planet/{id}")
    public ResponseEntity<String> deletePlanet(@PathVariable Long id) {
        // delete a planet and check for errors such as no input and invlaid input
        Planet planet = planetService.getPlanet(id);
        if (planet != null) {
            planetService.deletePlanet(id);
            return new ResponseEntity<>("Planet deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Planet not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/planet/{id}")
    @ResponseBody
    public ResponseEntity<Planet> getPlanet(@PathVariable Long id) {
        // retrieve a planet based on their id
        return new ResponseEntity<>(planetService.getPlanet(id), HttpStatus.OK);
    }

    @GetMapping("/createNewPlanet")
    public String createNewPlanet() {
        // initialize the create new planet view
        return "createNewPlanet";
    }

    @PostMapping("/planet")
    @ResponseBody
    public ResponseEntity<String> createPlanet(@RequestBody Planet planet) {
        // create a new planet and make sure the inputs for homeplanet and starship are
        // valid
        try {
            // Save to database
            planetService.savePlanet(planet);

            // Append to NDJSON file
            Files.write(Paths.get(FILE_PATH), (planet.toNdjson() + "\n").getBytes(),
                    java.nio.file.StandardOpenOption.APPEND);

            return new ResponseEntity<>("Planet created successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to create planet", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/maxPlanetId")
    public ResponseEntity<Long> getMaxPlanetId() {
        // find the max planet id
        Long maxId = planetService.getMaxPlanetId();
        return ResponseEntity.ok(maxId);
    }
}
