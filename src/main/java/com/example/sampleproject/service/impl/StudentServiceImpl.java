package com.example.sampleproject.service.impl;

import com.example.sampleproject.entity.Student;
import com.example.sampleproject.entity.Teacher;
import com.example.sampleproject.repository.StudentRepository;
import com.example.sampleproject.repository.TeacherRepository;
import com.example.sampleproject.service.StudentService;
import com.example.sampleproject.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    @Override
    public Student updateStudent(Student student) {

        Student existingStudent = studentRepository.findById(student.getStudentId()).orElse(null);

        existingStudent.setStudentName(student.getStudentName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setDepartmentName(student.getDepartmentName());
        existingStudent.setPhoneNumber(student.getPhoneNumber());

        return studentRepository.save(existingStudent);

    }

    @Override
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @Override
    public Student addTeacherToStudent(Long teacherId, Long studentId) {

        Student existingStudent = studentRepository.findById(studentId).get();

        Teacher existingTeacher = teacherRepository.findById(teacherId).get();

        existingStudent.setTeacher(existingTeacher);

        return studentRepository.save(existingStudent);
    }
}
