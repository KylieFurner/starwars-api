package com.kyliescode.starwars_api_challenge.model;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String climate;
    private int population;

    /**
     * Convert the Planet object to NDJSON format.
     * NDJSON (Newline Delimited JSON) is a JSON format where each line is a
     * separate JSON object.
     *
     * @return NDJSON representation of the Planet object
     * @throws IOException if there's an error during JSON serialization
     */
    public String toNdjson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(this);
    }
}
