package com.example.sampleproject.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestModel {
    private String name;
    private String email;
    private String phoneNumber;
    private String departmentName;
    private String password;
}
