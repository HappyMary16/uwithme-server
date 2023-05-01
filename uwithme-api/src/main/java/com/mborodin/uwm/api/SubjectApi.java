package com.mborodin.uwm.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * TODO: add description.
 *
 * @author mariia.borodin (mborodin)
 * @since 1.1
 */
@Getter
@AllArgsConstructor
public class SubjectApi {

    private Long subjectId;
    private String teacherId;
    private String subjectName;
}
