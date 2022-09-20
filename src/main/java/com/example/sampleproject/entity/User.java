package com.example.sampleproject.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_table")
public class User {

    @Id
    String userName;

    String name;

    boolean enable;

    private String userPassword;

    @ManyToMany(
            fetch = FetchType.EAGER, cascade = CascadeType.ALL
    )
    @JoinTable(
            name = "user_role_table",
            joinColumns = {
                    @JoinColumn(name = "user_id"),
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "role_id")
            }
    )
    private Set<Role> roles;
}
