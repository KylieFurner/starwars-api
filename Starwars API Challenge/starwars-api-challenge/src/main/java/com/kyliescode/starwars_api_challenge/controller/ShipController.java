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

import com.kyliescode.starwars_api_challenge.model.Ship;
import com.kyliescode.starwars_api_challenge.service.ShipService;

@Controller
public class ShipController {
    // controller class for all ships.
    private final ShipService shipService;
    private static final String FILE_PATH = "C:\\Users\\Kylie\\Starwars API Challenge\\starwars-api-challenge\\jsonFiles\\starship_master.ndjson";

    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping("/allShips")
    public String getAllShips(Model model) {
        // controller class for all ships.
        List<Ship> ships = shipService.findAll();
        model.addAttribute("ships", ships);
        return "allShips";
    }

    @GetMapping("/searchShip")
    public String searchShipPage() {
        // initialize the search view for ships
        return "searchShip";
    }

    @PutMapping("/ship/{id}")
    @ResponseBody
    public ResponseEntity<Ship> updateShip(@PathVariable Long id, @RequestBody Ship updatedShip) {
        // after searching a ship find it by its index to update it
        Ship ship = shipService.getShip(id);
        // check for errors such as no input and invlaid input
        if (ship != null) {
            ship.setName(updatedShip.getName());
            ship.setModel(updatedShip.getModel());
            ship.setCostInCredits(updatedShip.getCostInCredits());

            Ship savedShip = shipService.saveShip(ship);
            return new ResponseEntity<>(savedShip, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/ship/{id}")
    public ResponseEntity<String> deleteShip(@PathVariable Long id) {
        // delete a ship and check for errors such as no input and invlaid input
        Ship ship = shipService.getShip(id);
        if (ship != null) {
            shipService.deleteShip(id);
            return new ResponseEntity<>("Ship deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Ship not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/ship/{id}")
    @ResponseBody
    public ResponseEntity<Ship> getShip(@PathVariable Long id) {
        // retrieve a ship based on their id
        return new ResponseEntity<>(shipService.getShip(id), HttpStatus.OK);
    }

    @GetMapping("/createNewShip")
    public String createNewShip() {
        // initialize the create new ship view
        return "createNewShip";
    }

    @PostMapping("/ship")
    @ResponseBody
    public ResponseEntity<String> createShip(@RequestBody Ship ship) {
        // create a new ship and make sure the inputs for homeplanet and starship
        // are valid
        try {
            // Save to database
            shipService.saveShip(ship);

            // Append to NDJSON file
            Files.write(Paths.get(FILE_PATH), (ship.toNdjson() + "\n").getBytes(),
                    java.nio.file.StandardOpenOption.APPEND);

            return new ResponseEntity<>("Ship created successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to create ship", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/maxShipId")
    public ResponseEntity<Long> getMaxShipId() {
        // find the max ship id
        Long maxId = shipService.getMaxShipId();
        return ResponseEntity.ok(maxId);
    }
}