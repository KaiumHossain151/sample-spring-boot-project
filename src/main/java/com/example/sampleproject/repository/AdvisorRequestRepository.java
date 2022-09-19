package com.example.sampleproject.repository;

import com.example.sampleproject.entity.AdvisorRequest;
import com.example.sampleproject.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvisorRequestRepository extends JpaRepository<AdvisorRequest,Long> {
    public AdvisorRequest findByTeacherId(Long id);
}
