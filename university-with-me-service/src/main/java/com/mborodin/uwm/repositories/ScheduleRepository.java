package com.mborodin.uwm.repositories;

import java.util.List;

import com.mborodin.uwm.model.persistence.ScheduleDb;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends CrudRepository<ScheduleDb, Long> {

    @Query("SELECT schedule " +
            "FROM  ScheduleDb schedule " +
            "JOIN ScheduleGroupDb scheduleGroup " +
            "ON schedule.id = scheduleGroup.scheduleId " +
            "WHERE scheduleGroup.groupId = :groupId")
    List<ScheduleDb> findAllByStudyGroupId(final Long groupId);

    @Query("SELECT schedule " +
            "FROM  ScheduleDb schedule " +
            "WHERE schedule.subject.teacher.id = :teacherId")
    List<ScheduleDb> findAllByTeacherId(@Param("teacherId") final String teacherId);
}
