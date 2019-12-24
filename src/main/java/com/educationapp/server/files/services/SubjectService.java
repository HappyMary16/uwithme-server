package com.educationapp.server.files.services;

import java.util.List;
import java.util.stream.Collectors;

import com.educationapp.server.files.models.persistence.Subject;
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

    public List<String> findSubjectNamesByTeacherUsername(final String username) {
        final Long teacherId = userRepository.findByUsername(username).get().getId();

        return subjectRepository.findAllByTeacherId(teacherId)
                                .stream()
                                .map(Subject::getName)
                                .collect(Collectors.toList());
    }
}
