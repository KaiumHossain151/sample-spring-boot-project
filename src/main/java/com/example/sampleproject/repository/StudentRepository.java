package com.example.sampleproject.repository;

import com.example.sampleproject.entity.Student;
import com.example.sampleproject.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {

}
