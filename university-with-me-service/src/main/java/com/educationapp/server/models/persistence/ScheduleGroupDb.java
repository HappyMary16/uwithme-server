package com.educationapp.server.models.persistence;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ScheduleGroupPK.class)
@Table(name = "schedule_group")
public class ScheduleGroupDb {

    @Id
    @Column(name = "schedule_id")
    private Long scheduleId;

    @Id
    @Column(name = "group_id")
    private Long groupId;
}
