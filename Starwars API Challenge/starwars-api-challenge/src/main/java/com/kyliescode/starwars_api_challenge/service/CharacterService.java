package com.kyliescode.starwars_api_challenge.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

// import javas List function
import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.kyliescode.starwars_api_challenge.exception.ResourceNotFound;
import com.kyliescode.starwars_api_challenge.model.Character;
import com.kyliescode.starwars_api_challenge.repository.CharacterRepository;
// Import the ResourceNotFound, Character, CharacterRepository, PlanetRepository, and ShipRepository classes

@Service
public class CharacterService {

    private final CharacterRepository characterRepository;
    private final ObjectMapper objectMapper;
    private static final String FILE_PATH = "C:\\Users\\Kylie\\Starwars API Challenge\\starwars-api-challenge\\jsonFiles\\star_wars_characters.ndjson";

    public CharacterService(CharacterRepository characterRepository, ObjectMapper objectMapper) {
        this.characterRepository = characterRepository;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void importCharacters() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(
                FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Character character = objectMapper.readValue(line, Character.class);
                characterRepository.save(character);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Character> findAll() {
        return characterRepository.findAll();
    }

    public Character getCharacter(Long id) {
        // retrieve a character by finding its ID. Throw an Exception if the character
        // is not found
        return characterRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Character not found"));
    }

    public Character saveCharacter(Character character) {
        // save a newly created character to the CharacterRepository
        return characterRepository.save(character);
    }

    public void deleteCharacter(Long id) {
        characterRepository.deleteById(id);
        try {
            List<String> lines = Files.lines(Paths.get(FILE_PATH))
                    .filter(line -> {
                        Character character = mapToCharacter(line);
                        return character == null || !character.getId().equals(id);
                    })
                    .collect(Collectors.toList());

            Files.write(Paths.get(FILE_PATH), lines);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle file deletion error as needed
        }
    }

    private Character mapToCharacter(String line) {
        try {
            // Use ObjectMapper to map JSON string to Character object
            return objectMapper.readValue(line, Character.class);
        } catch (MismatchedInputException e) {
            // Handle exception if there's a mismatch between the JSON content and the
            // Character class
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            // Handle other IO exceptions
            e.printStackTrace();
            return null;
        }
    }

    public Long getMaxCharacterId() {
        // find and return the max character ID
        return characterRepository.findMaxId();
    }
}
