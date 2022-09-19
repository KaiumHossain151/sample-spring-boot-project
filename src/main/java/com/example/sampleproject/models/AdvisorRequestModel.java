package com.example.sampleproject.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdvisorRequestModel {
    private Long teacherId;
    private Long studentId;
}
