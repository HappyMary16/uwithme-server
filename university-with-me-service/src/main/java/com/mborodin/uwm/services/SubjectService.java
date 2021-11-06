package com.mborodin.uwm.services;

import static com.mborodin.uwm.api.enums.Role.ROLE_STUDENT;
import static com.mborodin.uwm.api.enums.Role.ROLE_TEACHER;
import static com.mborodin.uwm.security.UserContextHolder.hasRole;

import java.util.List;
import java.util.stream.Collectors;

import com.mborodin.uwm.models.persistence.AccessToFileDB;
import com.mborodin.uwm.models.persistence.SubjectDB;
import com.mborodin.uwm.models.persistence.UserDb;
import com.mborodin.uwm.repositories.AccessToFileRepository;
import com.mborodin.uwm.repositories.FileRepository;
import com.mborodin.uwm.repositories.SubjectRepository;
import com.mborodin.uwm.repositories.UserRepository;
import com.mborodin.uwm.security.UserContextHolder;
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
        if (hasRole(ROLE_STUDENT)) {
            final List<AccessToFileDB> accessToFileDBS =
                    accessToFileRepository.findAllByStudyGroupId(UserContextHolder.getGroupId());

            return accessToFileDBS.stream()
                                  .map(accessToFileDB -> fileRepository.findById(accessToFileDB.getFileId())
                                                                       .get()
                                                                       .getSubjectId())
                                  .distinct()
                                  .map(subjectId -> subjectRepository.findById(subjectId).get())
                                  .collect(Collectors.toList());

        } else if (hasRole(ROLE_TEACHER)) {
            return subjectRepository.findAllByTeacherId(UserContextHolder.getId());
        }

        throw new UnsupportedOperationException("Operation 'Get files' supported only for teachers and students");
    }

    public SubjectDB save(final String userId, final String subject) {
        final UserDb teacher = userRepository.getProxyByIdIfExist(userId);
        return subjectRepository.save(new SubjectDB(subject, teacher));
    }
}
