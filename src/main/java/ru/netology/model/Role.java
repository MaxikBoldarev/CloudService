package ru.netology.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table (name = "Roles")

public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column (nullable = false,unique = true)
    private Long id;
    @Column (nullable = false)
    private String fileName;
    @Column (nullable = false)
    private String fileType;
    @Column (nullable = false)
    private String filePath;
    @Column (nullable = false)
    private byte [] fileSize;

    @ManyToOne
    private User user;


}
