package com.example.sampleproject.controller;

import com.example.sampleproject.entity.AdvisorRequest;
import com.example.sampleproject.entity.Student;
import com.example.sampleproject.entity.Teacher;
import com.example.sampleproject.models.AdvisorRequestModel;
import com.example.sampleproject.models.CustomResponse;
import com.example.sampleproject.models.StudentResponseModel;
import com.example.sampleproject.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    private AppService appService;

    @PostMapping("/teacher/create")
    public Teacher createTeacher(@RequestBody Teacher teacher){
        return appService.saveTeacher(teacher);
    }
    @GetMapping("/teacher")
    public List<Teacher> getTeachers(){
        return appService.getTeachers();
    }


    @GetMapping("/teacher/assigned/students")
    public List<StudentResponseModel> getAssignedStudents(@RequestParam Long id){
        return appService.getAssignedStudents(id);
    }

    @PostMapping("/assign/advisor")
    public CustomResponse assignAdvisor(@RequestParam Long teacherId, @RequestParam Long studentId){
        return appService.addStudentToTeacher(teacherId,studentId);
    }

    @PutMapping("/teacher/update")
    public Teacher updateTeacher(@RequestBody Teacher teacher){
        return appService.updateTeacher(teacher);
    }

    @PostMapping("/student/create")
    public Student createStudent(@RequestBody Student student){
        return appService.saveStudent(student);
    }
    @GetMapping("/student")
    public List<Student> getStudents(){
        return appService.getStudents();
    }

    @PutMapping("/student/update")
    public Student updateStudent(@RequestBody Student student){
        return appService.updateStudent(student);
    }

    @PostMapping("/advisor/request/send")
    public CustomResponse sendAdvisorRequest(@RequestBody AdvisorRequestModel model){
        return appService.sendAdvisorRequest(model);
    }

    @GetMapping("/advisor/request")
    public List<StudentResponseModel> getAllRequestsForSpecificTeacher(@RequestParam Long teacherId){
       return appService.getAllRequestsForSpecificTeacher(teacherId);
    }

    @DeleteMapping("/advisor/request/delete")
    public CustomResponse deleteRequest(@RequestBody AdvisorRequestModel model){
        return appService.deleteRequest(model);
    }

    @DeleteMapping("/advisor/delete")
    public CustomResponse deleteFromAdvisoryList(@RequestBody AdvisorRequestModel model){
       return appService.deleteFromAdvisoryList(model);
    }

}
