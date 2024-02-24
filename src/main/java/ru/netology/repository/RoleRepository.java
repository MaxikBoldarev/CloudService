package ru.netology.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import ru.netology.model.Role;

@Service
public interface RoleRepository extends JpaRepository<Role, Long> {}