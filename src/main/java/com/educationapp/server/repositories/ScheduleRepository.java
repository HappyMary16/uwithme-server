package com.educationapp.server.repositories;

import java.util.List;

import com.educationapp.server.models.persistence.ScheduleDb;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends CrudRepository<ScheduleDb, Long> {

    @Query(value = "SELECT * FROM schedule s " +
            "JOIN schedule_group sg ON s.id = sg.schedule_id " +
            "WHERE sg.group_id = :groupId",
            nativeQuery = true)
    List<ScheduleDb> findAllByStudyGroupId(final Long groupId);

    @Query(value = "SELECT * FROM schedule JOIN subjects ON schedule.subject_id = subjects.id WHERE subjects" +
            ".teacher_id = :teacherId",
            nativeQuery = true)
    List<ScheduleDb> findAllByTeacherId(@Param("teacherId") final Long teacherId);
}
