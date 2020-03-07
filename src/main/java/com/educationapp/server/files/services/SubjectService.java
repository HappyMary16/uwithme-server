package com.educationapp.server.files.services;

import static com.educationapp.server.common.enums.Role.STUDENT;
import static com.educationapp.server.common.enums.Role.TEACHER;

import java.util.List;
import java.util.stream.Collectors;

import com.educationapp.server.files.models.persistence.AccessToFileDB;
import com.educationapp.server.files.models.persistence.SubjectDB;
import com.educationapp.server.files.repositories.AccessToFileRepository;
import com.educationapp.server.files.repositories.FileRepository;
import com.educationapp.server.files.repositories.SubjectRepository;
import com.educationapp.server.users.model.persistence.StudentDB;
import com.educationapp.server.users.model.persistence.UserDB;
import com.educationapp.server.users.repositories.StudentRepository;
import com.educationapp.server.users.repositories.UserRepository;
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
