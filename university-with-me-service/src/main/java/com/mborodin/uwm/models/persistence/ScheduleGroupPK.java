package com.mborodin.uwm.models.persistence;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleGroupPK implements Serializable {

    protected Integer scheduleId;
    protected Integer groupId;
}
