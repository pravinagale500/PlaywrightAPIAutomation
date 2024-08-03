package com.api.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data  // Generates getters, setters, toString, equals, and hashCode methods
@AllArgsConstructor  // Generates a constructor with all fields
@NoArgsConstructor   // Generates a no-args constructor
@Builder  // Implements the builder pattern


public class Users {
    private String id;
    private String name;
    private String email;
    private String gender;
    private String status;
}

