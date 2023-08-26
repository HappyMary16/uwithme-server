package com.mborodin.uwm.services;

import static com.mborodin.uwm.api.enums.Role.ROLE_STUDENT;
import static com.mborodin.uwm.api.enums.Role.ROLE_TEACHER;
import static com.mborodin.uwm.security.UserContextHolder.getGroupId;
import static com.mborodin.uwm.security.UserContextHolder.getId;
import static com.mborodin.uwm.security.UserContextHolder.hasRole;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.mborodin.uwm.api.SubjectApi;
import com.mborodin.uwm.model.mapper.SubjectMapper;
import com.mborodin.uwm.model.persistence.AccessToFileDB;
import com.mborodin.uwm.model.persistence.FileDB;
import com.mborodin.uwm.model.persistence.SubjectDB;
import com.mborodin.uwm.model.persistence.SubjectTeacherAssociationDb;
import com.mborodin.uwm.repositories.AccessToFileRepository;
import com.mborodin.uwm.repositories.FileRepository;
import com.mborodin.uwm.repositories.SubjectRepository;
import com.mborodin.uwm.repositories.SubjectTeacherAssociationRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectTeacherAssociationRepository subjectTeacherAssociationRepository;
    private final AccessToFileRepository accessToFileRepository;
    private final FileRepository fileRepository;
    private final SubjectMapper subjectMapper;

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public void updateSubjectTeacherAssociations() {
        ((Collection<SubjectDB>) subjectRepository.findAll())
                .stream()
                .map(subjectDB -> Pair.of(subjectDB.getId(), subjectDB.getTeacherId()))
                .distinct()
                .forEach(pair -> saveSubjectTeacherAssociationIfNotExists(pair.getFirst(), pair.getSecond()));
    }

    public Optional<SubjectApi> findById(final long id) {
        return subjectRepository.findById(id)
                                .map(subjectMapper::toApi);
    }

    public List<SubjectApi> findSubjects(final Long universityId) {
        return subjectRepository.findAllByUniversityId(universityId)
                                .stream()
                                .map(subjectMapper::toApi)
                                .collect(Collectors.toList());
    }

    public List<SubjectApi> findUsersSubjects() {
        if (!hasRole(ROLE_STUDENT) && !hasRole(ROLE_TEACHER)) {
            throw new UnsupportedOperationException("Operation 'Get files' supported only for teachers and students");
        }

        final Set<Long> subjectIds;

        if (hasRole(ROLE_STUDENT)) {
            subjectIds = accessToFileRepository.findAllByStudyGroupId(getGroupId())
                                               .stream()
                                               .map(AccessToFileDB::getFileId)
                                               .map(fileRepository::findById)
                                               .filter(Optional::isPresent)
                                               .map(Optional::get)
                                               .map(FileDB::getSubjectId)
                                               .collect(Collectors.toSet());

        } else {
            subjectIds = subjectTeacherAssociationRepository.findAllByTeacherId(getId())
                                                            .stream()
                                                            .map(SubjectTeacherAssociationDb::getSubjectId)
                                                            .collect(Collectors.toSet());
        }

        return subjectRepository.findAllByIdIn(subjectIds)
                                .stream()
                                .map(subjectMapper::toApi)
                                .collect(Collectors.toList());
    }

    public SubjectApi save(final String userId, final String subjectName) {
        final var subject = subjectRepository.save(new SubjectDB(subjectName, userId));
        saveSubjectTeacherAssociationIfNotExists(subject.getId(), userId);
        return subjectMapper.toApi(subject);
    }

    public void saveSubjectTeacherAssociationIfNotExists(final Long subjectId, final String teacherId) {
        if (!subjectTeacherAssociationRepository.existsBySubjectIdAndTeacherId(subjectId, teacherId)) {
            subjectTeacherAssociationRepository.save(SubjectTeacherAssociationDb.builder()
                                                                                .subjectId(subjectId)
                                                                                .teacherId(teacherId)
                                                                                .build());
        }
    }
}
