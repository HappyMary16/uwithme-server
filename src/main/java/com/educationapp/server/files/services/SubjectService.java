package com.educationapp.server.files.services;

import java.util.List;

import com.educationapp.server.files.models.persistence.SubjectDB;
import com.educationapp.server.files.repositories.SubjectRepository;
import com.educationapp.server.users.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    public List<SubjectDB> findSubjectNamesByTeacherUsername(final String username) {
        final Long teacherId = userRepository.findByUsername(username).get().getId();

        return subjectRepository.findAllByTeacherId(teacherId);
    }

    public void save(final String username, final String subject) {
        final Long teacherId = userRepository.findByUsername(username).get().getId();

        subjectRepository.save(new SubjectDB(subject, teacherId));
    }
}
