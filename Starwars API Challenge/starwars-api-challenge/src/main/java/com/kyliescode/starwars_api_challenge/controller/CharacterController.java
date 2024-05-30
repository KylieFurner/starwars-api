package com.kyliescode.starwars_api_challenge.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.kyliescode.starwars_api_challenge.model.Character;
import com.kyliescode.starwars_api_challenge.service.CharacterService;
import com.kyliescode.starwars_api_challenge.service.PlanetService;
import com.kyliescode.starwars_api_challenge.service.ShipService;

@Controller
public class CharacterController {
    // controller class for all characters. It also connects to the planet and ship
    // services because the homeplanet and starships field match to the indexes
    private final CharacterService characterService;
    @Autowired
    private PlanetService planetService;
    @Autowired
    private ShipService shipService;
    private static final String FILE_PATH = "C:\\Users\\Kylie\\Starwars API Challenge\\starwars-api-challenge\\jsonFiles\\star_wars_characters.ndjson";

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    @GetMapping("/allCharacters")
    public String getAllCharacters(Model model) {
        // controller class for all characters.
        List<Character> characters = characterService.findAll();
        model.addAttribute("characters", characters);
        return "allCharacters";
    }

    @GetMapping("/searchCharacter")
    public String searchCharacterPage() {
        // initialize the search view for characters
        return "searchCharacter";
    }

    @PutMapping("/character/{id}")
    @ResponseBody
    public ResponseEntity<Character> updateCharacter(@PathVariable Long id, @RequestBody Character updatedCharacter) {
        // after searching a character find it by its index to update it
        Character character = characterService.getCharacter(id);
        // check for errors such as no input and invlaid input
        if (character != null) {
            character.setName(updatedCharacter.getName());
            character.setHomePlanet(updatedCharacter.getHomePlanet());
            character.setStarships(updatedCharacter.getStarships());

            Character savedCharacter = characterService.saveCharacter(character);
            return new ResponseEntity<>(savedCharacter, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/character/{id}")
    public ResponseEntity<String> deleteCharacter(@PathVariable Long id) {
        // delete a character and check for errors such as no input and invlaid input
        Character character = characterService.getCharacter(id);
        if (character != null) {
            characterService.deleteCharacter(id);
            return new ResponseEntity<>("Character deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Character not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/character/{id}")
    @ResponseBody
    public ResponseEntity<Character> getCharacter(@PathVariable Long id) {
        // retrieve a character based on their id
        return new ResponseEntity<>(characterService.getCharacter(id), HttpStatus.OK);
    }

    @GetMapping("/createNewCharacter")
    public String createNewCharacter() {
        // initialize the create new character view
        return "createNewCharacter";
    }

    @PostMapping("/character")
    public ResponseEntity<String> createCharacter(@RequestBody Character character) {
        // create a new character and make sure the inputs for homeplanet and starship
        // are valid
        Long maxPlanetId = planetService.getMaxPlanetId();
        Long maxShipId = shipService.getMaxShipId();
        try {
            // check if the homePlanet is in range (not greater than the highest index) and
            // raise an alert if it is
            Long homePlanetId = Long.parseLong(character.getHomePlanet());
            if (homePlanetId < 0 || homePlanetId > maxPlanetId) {
                return new ResponseEntity<>("Invalid homePlanet ID.", HttpStatus.BAD_REQUEST);
            }
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("HomePlanet should be a number.", HttpStatus.BAD_REQUEST);
        }
        try {
            // check if the starships is in range (not greater than the highest index) and
            // raise an alert if it is
            Long starshipsId = Long.parseLong(character.getStarships());
            if (starshipsId < 0 || starshipsId > maxShipId) {
                return new ResponseEntity<>("Invalid starship ID.", HttpStatus.BAD_REQUEST);
            }
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Starship should be a number.", HttpStatus.BAD_REQUEST);
        }
        try {
            // Save to database
            characterService.saveCharacter(character);
            // Append to NDJSON file
            Files.write(Paths.get(FILE_PATH), (character.toNdjson() + "\n").getBytes(),
                    java.nio.file.StandardOpenOption.APPEND);

            return new ResponseEntity<>("Character created successfully", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>("Failed to create character", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/maxCharacterId")
    public ResponseEntity<Long> getMaxCharacterId() {
        // find the max character id
        Long maxId = characterService.getMaxCharacterId();
        return ResponseEntity.ok(maxId);
    }

}
