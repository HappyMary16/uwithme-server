package com.educationapp.server.models.api.admin;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteLessonApi {

    private final Long lessonId;

    private final List<String> groups;
}
