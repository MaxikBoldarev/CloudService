package ru.netology.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.netology.entity.CloudFile;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<CloudFile, String> {

    @Query(value = "select * from depository s where s.user_id = ?1 order by s.id desc limit ?2", nativeQuery = true)
    List<CloudFile> findAllByUserIdWithLimit(long userId, int limit);
    Optional<CloudFile> findByUserIdAndName(long userId, String name);
    void removeByUserIdAndName(long userId, String name);
}