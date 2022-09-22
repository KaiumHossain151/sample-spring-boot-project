package com.example.sampleproject.controller;

import com.example.sampleproject.entity.Student;
import com.example.sampleproject.entity.Teacher;
import com.example.sampleproject.models.*;
import com.example.sampleproject.service.AppService;
import com.example.sampleproject.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
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


    @PostMapping("/teacher/assigned/students")
    public List<StudentResponseModel> getAssignedStudents(@RequestBody RequestModel model){
        return appService.getAssignedStudents(model.getUserId());
    }

    @PostMapping("/assign/advisor")
    public CustomResponse assignAdvisor(@RequestBody AdvisorRequestModel model){
        return appService.addStudentToTeacher(model.getTeacherId(), model.getStudentId());
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

    @PostMapping("/advisor/request")
    public List<StudentResponseModel> getAllRequestsForSpecificTeacher(@RequestBody RequestModel requestModel){
       return appService.getAllRequestsForSpecificTeacher(requestModel.getUserId());
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
    public String deleteUser(@RequestBody RequestModelEmail modelEmail ){
        return appService.deleteUser(modelEmail.getEmail());
    }

    @PutMapping("user/deactivate")
    public String deactivateUser(@RequestBody RequestModelEmail modelEmail){
        return appService.deactivateUser(modelEmail.getEmail());
    }
    @PutMapping("user/activate")
    public String activateUser(@RequestBody RequestModelEmail modelEmail){
        return appService.activateUser(modelEmail.getEmail());
    }
}
