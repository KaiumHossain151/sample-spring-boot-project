package com.example.sampleproject.service;

import com.example.sampleproject.entity.AdvisorRequest;
import com.example.sampleproject.entity.Student;
import com.example.sampleproject.entity.Teacher;
import com.example.sampleproject.models.AdvisorRequestModel;
import com.example.sampleproject.models.CustomResponse;
import com.example.sampleproject.models.StudentResponseModel;
import com.example.sampleproject.repository.AdvisorRequestRepository;
import com.example.sampleproject.repository.StudentRepository;
import com.example.sampleproject.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppService {

    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AdvisorRequestRepository advisorRequestRepository;

    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
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

    public Student saveStudent(Student student) {
        return studentRepository.save(student);
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

}
