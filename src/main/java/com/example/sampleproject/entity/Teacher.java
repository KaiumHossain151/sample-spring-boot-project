package com.example.sampleproject.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(
        name = "teacher_table",
        uniqueConstraints = @UniqueConstraint(
                name = "email_unique",
                columnNames = "email_address"
        )
)
@ToString(exclude = "students")
public class Teacher {

    @Id
    @SequenceGenerator(
            name = "teacher_sequence",
            sequenceName = "teacher_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "teacher_sequence"
    )
    private Long teacherId;
    private String teacherName;

    @Column(
            name = "email_address",
            nullable = false
    )
    private String email;
    private String phoneNumber;
    private String departmentName;

    @JsonIgnore
    @OneToMany(
            cascade = CascadeType.ALL
    )
//    @JoinColumn(name = "teacher_id",referencedColumnName = "teacherId")
    private List<Student> students;
}
