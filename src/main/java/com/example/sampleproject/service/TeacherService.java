package com.example.sampleproject.service;

import com.example.sampleproject.entity.Student;
import com.example.sampleproject.entity.Teacher;

import java.util.List;

public interface TeacherService {
    Teacher saveTeacher(Teacher teacher);

    Teacher updateTeacher(Teacher teacher);

    List<Teacher> getTeachers();

    List<Student> getAssignedStudents(Long id);

    Teacher addStudentToTeacher(Long teacherId, Long studentId);
}
