package com.example.database_project.repository;

import com.example.database_project.entity.FavoriteEntity;
import com.example.database_project.entity.MemberEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO favorite_list (member_id, list_id) VALUES (?1, ?2)", nativeQuery = true)
    void saveFavorite(Long member_id, Long list_id);

    @Modifying
    @Transactional
    @Query(value = "DELETE from favorite_list where member_id = ?1 and list_id = ?2", nativeQuery = true)
    void deleteFavorite(Long member_id, Long list_id);

    @Transactional
    @Query(value = "select * from favorite_list where member_id = ?1", nativeQuery = true)
    Optional<FavoriteEntity> findByMID(Long member_id);

    @Transactional
    @Query(value = "select * from favorite_list where list_id = ?1", nativeQuery = true)
    Optional<FavoriteEntity> findByLId(Long list_id);

    @Transactional
    @Query(value = "select * from favorite_list where member_id = ?1 and list_id = ?2", nativeQuery = true)
    Optional<FavoriteEntity> findByBothID(Long member_id, Long list_id);
}