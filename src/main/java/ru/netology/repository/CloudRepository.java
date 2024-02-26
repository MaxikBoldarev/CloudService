package ru.netology.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.netology.model.Role;

import java.util.Optional;

@Repository
public interface CloudRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByFileName (String name);


}
