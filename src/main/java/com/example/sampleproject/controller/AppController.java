package com.example.sampleproject.controller;

import com.example.sampleproject.entity.AdvisorRequest;
import com.example.sampleproject.entity.Student;
import com.example.sampleproject.entity.Teacher;
import com.example.sampleproject.models.*;
import com.example.sampleproject.service.AppService;
import com.example.sampleproject.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AppController {

    @Autowired
    private AppService appService;

    @Autowired
    JwtService jwtService;

    @PostConstruct
    public void initRoleAndAdmin(){
        appService.initRoleAndAdmin();
    }


    @PostMapping({"/authenticate"})
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        return jwtService.createJwtToken(jwtRequest);
    }

    @PostMapping("/teacher/create")
    public Object createTeacher(@RequestBody UserRequestModel teacher){
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
    public Object createStudent(@RequestBody UserRequestModel student){
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

    @PostMapping("/user/password/change")
    public String changePassword(@RequestBody PasswordModel passwordModel){
        return appService.changePassword(passwordModel);
    }

    @DeleteMapping("user/delete")
    public String deleteUser(@RequestParam String emailAddress){
        return appService.deleteUser(emailAddress);
    }

    @PutMapping("user/deactivate")
    public String deactivateUser(@RequestParam String emailAddress){
        return appService.deactivateUser(emailAddress);
    }
    @PutMapping("user/activate")
    public String activateUser(@RequestParam String emailAddress){
        return appService.activateUser(emailAddress);
    }
}
