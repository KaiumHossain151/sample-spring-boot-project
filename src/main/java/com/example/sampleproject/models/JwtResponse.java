package com.example.sampleproject.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {

    private boolean status;
    private String message;
    private String userName;
    private String name;
    private Long id;
    private String role;
    private String token;

}
