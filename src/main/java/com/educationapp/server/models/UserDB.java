package com.educationapp.server.models;

import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "users")
public class UserDB implements Serializable {

    private static final long serialVersionUID = 3366295050169335755L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @UniqueElements
    @Column(name = "nickname")
    private String nickname;

    @Column(name = "password")
    private String password;

    @UniqueElements
    @Column(name = "phone")
    private String phone;

    @UniqueElements
    @Column(name = "email")
    private String email;
}
