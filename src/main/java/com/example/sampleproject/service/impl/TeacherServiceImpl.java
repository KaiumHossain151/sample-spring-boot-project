package com.example.sampleproject.service.impl;

import com.example.sampleproject.entity.Student;
import com.example.sampleproject.entity.Teacher;
import com.example.sampleproject.repository.StudentRepository;
import com.example.sampleproject.repository.TeacherRepository;
import com.example.sampleproject.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }

    @Override
    public Teacher updateTeacher(Teacher teacher) {

        Teacher existingTeacher = teacherRepository.findById(teacher.getTeacherId()).orElse(null);

        existingTeacher.setTeacherName(teacher.getTeacherName());
        existingTeacher.setEmail(teacher.getEmail());
        existingTeacher.setDepartmentName(teacher.getDepartmentName());
        existingTeacher.setPhoneNumber(teacher.getPhoneNumber());
        existingTeacher.setStudents(teacher.getStudents());

        return teacherRepository.save(existingTeacher);

    }

    @Override
    public List<Teacher> getTeachers() {
        return teacherRepository.findAll();
    }

    @Override
    public List<Student> getAssignedStudents(Long id) {

        List<Student> students = teacherRepository.findById(id).get().getStudents();

        return students;
    }

    @Override
    public Teacher addStudentToTeacher(Long teacherId, Long studentId) {

        Student existingStudent = studentRepository.findById(studentId).get();

        Teacher existingTeacher = teacherRepository.findById(teacherId).get();

        existingTeacher.getStudents().add(existingStudent);

         return teacherRepository.save(existingTeacher);
    }
}
