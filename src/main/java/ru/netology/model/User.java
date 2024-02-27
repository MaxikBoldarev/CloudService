package ru.netology.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.File;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;

    @OneToMany(cascade = CascadeType.ALL)
    List<Role> userFiles;


}