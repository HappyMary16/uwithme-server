package com.educationapp.server.schedule.repositories;

import java.util.List;

import com.educationapp.server.schedule.models.ScheduleDb;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends CrudRepository<ScheduleDb, Long> {

    List<ScheduleDb> findAllByStudyGroupId(final Long groupId);

    List<ScheduleDb> findAllByStudyGroupId(final List<Long> groupId);

    @Query(value = "SELECT * FROM schedule JOIN subjects ON schedule.subject_id = subjects.id WHERE subjects" +
            ".teacher_id = :teacherId",
            nativeQuery = true)
    List<ScheduleDb> findAllByTeacherId(@Param("teacherId") final Long teacherId);
}
