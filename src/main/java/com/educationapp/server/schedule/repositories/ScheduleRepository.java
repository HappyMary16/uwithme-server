package com.educationapp.server.schedule.repositories;

import com.educationapp.server.schedule.models.ScheduleDb;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends CrudRepository<ScheduleDb, Long> {

}
