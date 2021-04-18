package com.educationapp.server.services;

import static com.educationapp.server.enums.Role.STUDENT;
import static com.educationapp.server.enums.Role.TEACHER;

import java.util.List;
import java.util.stream.Collectors;

import com.educationapp.server.enums.Role;
import com.educationapp.server.models.persistence.AccessToFileDB;
import com.educationapp.server.models.persistence.SubjectDB;
import com.educationapp.server.models.persistence.UserDb;
import com.educationapp.server.repositories.AccessToFileRepository;
import com.educationapp.server.repositories.FileRepository;
import com.educationapp.server.repositories.SubjectRepository;
import com.educationapp.server.repositories.UserRepository;
import com.educationapp.server.security.UserContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;
    private final AccessToFileRepository accessToFileRepository;
    private final FileRepository fileRepository;

    public List<SubjectDB> findUsersSubjects() {
        final Role userRole = UserContextHolder.getRole();
        if (STUDENT.equals(userRole)) {
            final List<AccessToFileDB> accessToFileDBS =
                    accessToFileRepository.findAllByStudyGroupId(UserContextHolder.getGroupId());

            return accessToFileDBS.stream()
                                  .map(accessToFileDB -> fileRepository.findById(accessToFileDB.getFileId())
                                                                       .get()
                                                                       .getSubjectId())
                                  .distinct()
                                  .map(subjectId -> subjectRepository.findById(subjectId).get())
                                  .collect(Collectors.toList());

        } else if (TEACHER.equals(userRole)) {
            return subjectRepository.findAllByTeacherId(UserContextHolder.getId());
        }

        throw new UnsupportedOperationException("Operation 'Get files' supported only for teachers and students");
    }

    public SubjectDB save(final String userId, final String subject) {
        final UserDb teacher = userRepository.getProxyByIdIfExist(userId);
        return subjectRepository.save(new SubjectDB(subject, teacher));
    }
}