package ru.netology.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.netology.model.File;
import ru.netology.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface CloudRepository extends JpaRepository<File,Long> {
    Optional<File> findByFileName (User user, String name);
    List<File> findAllByUser(User user, Sort sort);
    void removeByUserAndFilename(User user, String filename);

    @Modifying
    @Query("update File f set f.filename = :newName where f.filename = :filename and f.user = :user")
    void editFileNameByUser(@Param("user") User user, @Param("filename") String filename, @Param("newName") String newName);


}
