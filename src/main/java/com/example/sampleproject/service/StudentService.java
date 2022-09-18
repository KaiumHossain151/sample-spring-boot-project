package com.example.sampleproject.service;

import com.example.sampleproject.entity.Student;
import com.example.sampleproject.entity.Teacher;

import java.util.List;

public interface StudentService {
    Student saveStudent(Student student);

    Student updateStudent(Student student);

    List<Student> getStudents();

    Student addTeacherToStudent(Long teacherId, Long studentId);
}
