package com.mborodin.uwm.model.persistence;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@IdClass(ScheduleGroupDb.ScheduleGroupPK.class)
@Table(name = "schedule_group")
public class ScheduleGroupDb {

    @Id
    @Column(name = "schedule_id")
    private Long scheduleId;
    @Id
    @Column(name = "group_id")
    private Long groupId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ScheduleGroupPK implements Serializable {

        protected Integer scheduleId;
        protected Integer groupId;
    }
}
