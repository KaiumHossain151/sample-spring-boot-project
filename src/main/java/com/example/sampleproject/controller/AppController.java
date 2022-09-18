package com.example.sampleproject.controller;

import com.example.sampleproject.entity.Student;
import com.example.sampleproject.entity.Teacher;
import com.example.sampleproject.service.StudentService;
import com.example.sampleproject.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    @PostMapping("/teacher/create")
    public Teacher createTeacher(@RequestBody Teacher teacher){
        return teacherService.saveTeacher(teacher);
    }
    @GetMapping("/teacher")
    public List<Teacher> getTeachers(){
        return teacherService.getTeachers();
    }


    @GetMapping("/teacher/advisorylist")
    public List<Student> getAssignedStudents(@RequestParam Long id){
        return teacherService.getAssignedStudents(id);
    }

    @PostMapping("/teacher/assignstudent")
    public Teacher assignStudents(@RequestParam Long teacherId, @RequestParam Long studentId){
        return teacherService.addStudentToTeacher(teacherId,studentId);
    }

    @PutMapping("/teacher/update")
    public Teacher updateTeacher(@RequestBody Teacher teacher){
        return teacherService.updateTeacher(teacher);
    }

    @PostMapping("/student/create")
    public Student createTeacher(@RequestBody Student student){
        return studentService.saveStudent(student);
    }
    @GetMapping("/student")
    public List<Student> getStudents(){
        return studentService.getStudents();
    }

    @PutMapping("/student/update")
    public Student updateStudent(@RequestBody Student student){
        return studentService.updateStudent(student);
    }

    @PostMapping("/student/addteacher")
    public Student assignTeacher(@RequestParam Long teacherId, @RequestParam Long studentId){
        return studentService.addTeacherToStudent(teacherId,studentId);
    }
}
