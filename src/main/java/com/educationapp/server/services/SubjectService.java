package com.educationapp.server.services;

import static com.educationapp.server.enums.Role.STUDENT;
import static com.educationapp.server.enums.Role.TEACHER;

import java.util.List;
import java.util.stream.Collectors;

import com.educationapp.server.models.persistence.AccessToFileDB;
import com.educationapp.server.models.persistence.StudentDB;
import com.educationapp.server.models.persistence.SubjectDB;
import com.educationapp.server.models.persistence.UserDB;
import com.educationapp.server.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccessToFileRepository accessToFileRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private FileRepository fileRepository;

    public List<SubjectDB> findSubjectsByTeacherUsername(final String username) {
        final UserDB user = userRepository.findByUsername(username).get();
        if (user.getRole().equals(STUDENT.getId())) {
            final StudentDB student = studentRepository.findById(user.getId()).get();
            final List<AccessToFileDB> accessToFileDBS =
                    accessToFileRepository.findAllByStudyGroupId(student.getStudyGroupId());

            return accessToFileDBS.stream()
                                  .map(accessToFileDB -> fileRepository.findById(accessToFileDB.getFileId())
                                                                       .get()
                                                                       .getSubjectId())
                                  .distinct()
                                  .map(subjectId -> subjectRepository.findById(subjectId).get())
                                  .collect(Collectors.toList());

        } else if (user.getRole().equals(TEACHER.getId())) {
            return subjectRepository.findAllByTeacherId(user.getId());
        }

        throw new UnsupportedOperationException("Operation 'Get files' supported only for teachers and students");
    }

    public SubjectDB save(final String username, final String subject) {
        final Long teacherId = userRepository.findByUsername(username).get().getId();

        return subjectRepository.save(new SubjectDB(subject, teacherId));
    }
}