package com.example.sampleproject.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponseModel {

    private Long studentId;
    private String studentName;
    private String email;
    private String phoneNumber;
    private String departmentName;
}
