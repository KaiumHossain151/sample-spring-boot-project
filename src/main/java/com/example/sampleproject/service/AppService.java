package com.example.sampleproject.service;

import com.example.sampleproject.entity.*;
import com.example.sampleproject.models.*;
import com.example.sampleproject.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AppService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdvisorRequestRepository advisorRequestRepository;

    public Object saveTeacher(UserRequestModel userRequestModel) {

        User existingUser = userRepository.findByUserName(userRequestModel.getEmail());

        if (existingUser == null) {
            User user = new User();

            Role role = roleRepository.findByRoleName("Teacher");

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(role);

            user.setRoles(userRoles);
            user.setUserName(userRequestModel.getEmail());
            user.setEnable(true);
            user.setUserPassword(getEncodedPassword(userRequestModel.getPassword()));
            user.setName(userRequestModel.getName());

            userRepository.save(user);

            Teacher teacher = Teacher.builder()
                    .teacherName(userRequestModel.getName())
                    .email(userRequestModel.getEmail())
                    .departmentName(userRequestModel.getDepartmentName())
                    .phoneNumber(userRequestModel.getPhoneNumber()).build();

            return teacherRepository.save(teacher);
        }else {
            return new CustomResponse("User already exists",false);
        }
    }

    public Teacher updateTeacher(Teacher teacher) {

        Teacher existingTeacher = teacherRepository.findById(teacher.getTeacherId()).orElse(null);

        existingTeacher.setTeacherName(teacher.getTeacherName());
        existingTeacher.setEmail(teacher.getEmail());
        existingTeacher.setDepartmentName(teacher.getDepartmentName());
        existingTeacher.setPhoneNumber(teacher.getPhoneNumber());
        existingTeacher.setStudents(teacher.getStudents());

        return teacherRepository.save(existingTeacher);

    }

    public List<Teacher> getTeachers() {
        return teacherRepository.findAll();
    }

    public List<StudentResponseModel> getAssignedStudents(Long id) {

        List<Student> students = teacherRepository.findById(id).get().getStudents();

        List<StudentResponseModel>  studentResponseModelList = new ArrayList<>();

        if (students !=null && !students.isEmpty()) {

            for (Student student : students) {
                studentResponseModelList.add(StudentResponseModel.builder()
                        .studentName(student.getStudentName())
                        .studentId(student.getStudentId())
                        .email(student.getEmail())
                        .departmentName(student.getDepartmentName())
                        .phoneNumber(student.getPhoneNumber())
                        .build());
            }

            return studentResponseModelList;
        }else {
            return new ArrayList<>();
        }

    }

    public CustomResponse addStudentToTeacher(Long teacherId, Long studentId) {

        Student existingStudent = studentRepository.findById(studentId).get();

        Teacher existingTeacher = teacherRepository.findById(teacherId).get();

        if (existingStudent != null &&  existingTeacher != null){

            if (existingTeacher.getStudents() !=null
                    && !existingTeacher.getStudents().isEmpty()
                    && existingStudent.getTeacher() != null) {
                for (Student student : existingTeacher.getStudents()) {
                    if (student.getStudentId().equals(studentId) && existingStudent.getTeacher().getTeacherId().equals(teacherId)) {
                        return new CustomResponse("Already in advisory list.", false);
                    }
                }
            }

            existingTeacher.getStudents().add(existingStudent);

            existingStudent.setTeacher(existingTeacher);

            studentRepository.save(existingStudent);

            teacherRepository.save(existingTeacher);

            AdvisorRequest advisorRequest = advisorRequestRepository.findByTeacherId(teacherId);
            if (advisorRequest != null){
                advisorRequest.getStudents().remove(studentRepository.findById(studentId).get());

                if (advisorRequest.getStudents() != null && !advisorRequest.getStudents().isEmpty()){
                    advisorRequestRepository.save(advisorRequest);
                }else {
                    advisorRequestRepository.delete(advisorRequest);
                }
            }

            return new CustomResponse("Successfully added advisor.",true);
        }else {
            return new CustomResponse("Student and Teacher not found.",false);
        }

    }

    public Object saveStudent(UserRequestModel userRequestModel) {

        User existingUser = userRepository.findByUserName(userRequestModel.getEmail());

        if (existingUser == null) {

            User user = new User();

            Role role = roleRepository.findByRoleName("Student");

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(role);

            user.setRoles(userRoles);
            user.setUserName(userRequestModel.getEmail());
            user.setEnable(true);
            user.setUserPassword(getEncodedPassword(userRequestModel.getPassword()));
            user.setName(userRequestModel.getName());

            userRepository.save(user);

            Student student = Student.builder()
                    .studentName(userRequestModel.getName())
                    .email(userRequestModel.getEmail())
                    .departmentName(userRequestModel.getDepartmentName())
                    .phoneNumber(userRequestModel.getPhoneNumber()).build();

            return studentRepository.save(student);
        }else {
            return new CustomResponse("User already exists",false);
        }
    }

    public Student updateStudent(Student student) {

        Student existingStudent = studentRepository.findById(student.getStudentId()).orElse(null);

        existingStudent.setStudentName(student.getStudentName());
        existingStudent.setEmail(student.getEmail());
        existingStudent.setDepartmentName(student.getDepartmentName());
        existingStudent.setPhoneNumber(student.getPhoneNumber());

        return studentRepository.save(existingStudent);

    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public CustomResponse sendAdvisorRequest(AdvisorRequestModel model){
        AdvisorRequest advisorRequest = advisorRequestRepository.findByTeacherId(model.getTeacherId());

        if (advisorRequest != null){
            advisorRequest.setTeacherId(model.getTeacherId());

            for (Student student : advisorRequest.getStudents()){
                if (student.getStudentId().equals(model.getStudentId())){
                    return new CustomResponse("Already sent request before.",false);
                }
            }
            advisorRequest.getStudents().add(studentRepository.findById(model.getStudentId()).get());
            advisorRequestRepository.save(advisorRequest);

        }else {
            AdvisorRequest request = new AdvisorRequest();
            request.setTeacherId(model.getTeacherId());

            List<Student> list = new ArrayList<>();
            list.add(studentRepository.findById(model.getStudentId()).get());

            request.setStudents(list);
            advisorRequestRepository.save(request);
        }

        return new CustomResponse("Request sent.",true);

    }

    public List<StudentResponseModel> getAllRequestsForSpecificTeacher(Long teacherId){
        AdvisorRequest request = advisorRequestRepository.findByTeacherId(teacherId);

        if (request != null){
            List<StudentResponseModel>  studentResponseModelList = new ArrayList<>();

            for (Student student : request.getStudents()){
                studentResponseModelList.add(StudentResponseModel.builder()
                        .studentName(student.getStudentName())
                        .studentId(student.getStudentId())
                        .email(student.getEmail())
                        .departmentName(student.getDepartmentName())
                        .phoneNumber(student.getPhoneNumber())
                        .build());
            }

            return studentResponseModelList;
        }else {
            return new ArrayList<>();
        }
    }

    public CustomResponse deleteRequest(AdvisorRequestModel model){
        AdvisorRequest advisorRequest = advisorRequestRepository.findByTeacherId(model.getTeacherId());
        if (advisorRequest != null){
            advisorRequest.getStudents().remove(studentRepository.findById(model.getStudentId()).get());

            if (advisorRequest.getStudents() != null && !advisorRequest.getStudents().isEmpty()){
                advisorRequestRepository.save(advisorRequest);
            }else {
                advisorRequestRepository.delete(advisorRequest);
            }

            return new CustomResponse("Request Removed.",true);
        }else {
            return new CustomResponse("Request not found.",false);
        }
    }

    public CustomResponse deleteFromAdvisoryList(AdvisorRequestModel model){
        Student existingStudent = studentRepository.findById(model.getStudentId()).get();

        Teacher existingTeacher = teacherRepository.findById(model.getTeacherId()).get();

        if (existingStudent !=null && existingTeacher!=null){
            existingStudent.setTeacher(null);
            studentRepository.save(existingStudent);

            existingTeacher.getStudents().remove(existingStudent);
            teacherRepository.save(existingTeacher);

            return new CustomResponse("Student deleted from advisory list.",true);
        }else {
            return new CustomResponse("Student and Teacher not found.",false);
        }
    }


    public void initRoleAndAdmin(){

        List<Role> roles = roleRepository.findAll();

        if (roles == null || roles.isEmpty()) {
            roleRepository.save(Role.builder().roleName("Admin").build());
            roleRepository.save(Role.builder().roleName("Teacher").build());
            roleRepository.save(Role.builder().roleName("Student").build());
        }


        User existingUser = userRepository.findByUserName("admin@gmail.com");

        if (existingUser==null) {
            User user = new User();

            Role role = roleRepository.findByRoleName("Admin");

            Set<Role> userRoles = new HashSet<>();
            userRoles.add(role);
            user.setRoles(userRoles);

            user.setUserName("admin@gmail.com");
            user.setName("Admin User");
            user.setEnable(true);
            user.setUserPassword(passwordEncoder.encode("@dm!nUs3r"));

            userRepository.save(user);
        }
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    public String changePassword(PasswordModel passwordModel) {
        User user = userRepository.findByUserName(passwordModel.getEmail());

        if (user!=null) {
            if (passwordEncoder.matches(passwordModel.getOldPassword(), user.getUserPassword())) {
                user.setUserPassword(getEncodedPassword(passwordModel.getNewPassword()));
                userRepository.save(user);
                return "Password Changed Successfully.";
            } else {
                return "Doesn't matched old password.";
            }
        }else {
            return "User not found with this email.";
        }
    }

    public String deleteUser(String emailAddress) {

        Teacher teacher = teacherRepository.findByEmail(emailAddress);

        Student student = studentRepository.findByEmail(emailAddress);

        User user = userRepository.findByUserName(emailAddress);

        if (teacher != null){
            teacherRepository.delete(teacher);
        }

        if (student != null){
            studentRepository.delete(student);
        }

        if (user != null){
            userRepository.delete(user);
        }

        return "User deleted";
    }

    public String deactivateUser(String emailAddress) {

        User user = userRepository.findByUserName(emailAddress);

        if (user != null){
            user.setEnable(false);
            userRepository.save(user);
            return "User deactivated";
        }else {
            return "User not found!";
        }

    }

    public String activateUser(String emailAddress) {
        User user = userRepository.findByUserName(emailAddress);

        if (user != null){
            user.setEnable(true);
            userRepository.save(user);
            return "User activated";
        }else {
            return "User not found!";
        }
    }
}
