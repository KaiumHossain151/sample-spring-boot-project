package com.example.sampleproject.entity;


import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "advisor_request_table"
)
@ToString(exclude = "students")
public class AdvisorRequest {

    @Id
    @SequenceGenerator(
            name = "advisor_request_sequence",
            sequenceName = "advisor_request_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "advisor_request_sequence"
    )
    private Long id;

    private Long teacherId;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Student> students;
}
