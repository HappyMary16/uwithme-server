package com.mborodin.uwm.model.persistence;

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
    private Long scheduleId;
    @Id
    private Long groupId;
}
