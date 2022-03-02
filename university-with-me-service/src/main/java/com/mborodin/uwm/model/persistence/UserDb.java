package com.mborodin.uwm.model.persistence;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

import com.mborodin.uwm.api.enums.Role;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

@Data
@Entity
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class UserDb implements Serializable {

    private static final long serialVersionUID = 3366295050169335755L;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Type(type = "json")
    @Column
    private Set<Role> roles;

    @Column(name = "is_admin")
    private Boolean isAdmin;

    @Column(name = "university_id")
    private Long universityId;

    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "group_id")
    private Long groupId;
}
