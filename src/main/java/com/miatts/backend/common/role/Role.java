package com.miatts.backend.common.role;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.miatts.backend.common.appuser.AppUser;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Role {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String name;

//    private boolean isDefault;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<AppUser> appUsers;
}

