package com.educationapp.server.models.persistence;

import java.io.Serializable;

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
@Table(name = "students")
@SecondaryTable(name = "users")
public class StudentDataDb implements Serializable {

    private static final long serialVersionUID = 8421354192650411535L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "group_id")
    private Long studyGroupId;

    @Column(name = "student_id")
    private String studentId;

    @Column(table = "users", name = "first_name")
    private String firstName;

    @Column(table = "users", name = "last_name")
    private String lastName;

    @Column(table = "users", name = "surname")
    private String surname;

    @Column(table = "users", name = "username")
    private String username;

    @Column(table = "users", name = "password")
    private String password;

    @Column(table = "users", name = "phone")
    private String phone;

    @Column(table = "users", name = "email")
    private String email;

    @Column(table = "users", name = "role")
    private Integer role;

    @Column(table = "users", name = "is_admin")
    private Boolean isAdmin;

    @Column(table = "users", name = "university_id")
    private Long universityId;
}
