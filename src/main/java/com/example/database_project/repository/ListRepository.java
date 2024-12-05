package com.example.database_project.repository;

import com.example.database_project.entity.ListEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ListRepository extends JpaRepository<ListEntity, Long> {

    // INSERT 쿼리
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO list_table (user_id, list_title, list_type, list_menu, list_address, list_like, list_created_time, list_updated_time) " +
            "VALUES (:userId, :listTitle, :listType, :listMenu, :listAddress, :listLike, NOW(), NOW())",
            nativeQuery = true)
    void saveList(
            @Param("userId") String userId,
            @Param("listTitle") String listTitle,
            @Param("listType") String listType,
            @Param("listMenu") String listMenu,
            @Param("listAddress") String listAddress,
            @Param("listLike") int listLike);

    // UPDATE 쿼리
    @Modifying
    @Transactional
    @Query(value = "UPDATE list_table SET user_id = :userId, list_title = :listTitle, list_type = :listType, list_menu = :listMenu, list_address = :listAddress, list_like = :listLike, list_updated_time = NOW() " +
            "WHERE lid = :id",
            nativeQuery = true)
    void updateList(
            @Param("userId") String userId,
            @Param("listTitle") String listTitle,
            @Param("listType") String listType,
            @Param("listMenu") String listMenu,
            @Param("listAddress") String listAddress,
            @Param("listLike") int listLike,
            @Param("id") Long id);


    // SELECT 쿼리 (전체 리스트 조회)
    @Query(value = "SELECT * FROM list_table", nativeQuery = true)
    List<ListEntity> findlistAll();

    // SELECT 쿼리 (특정 ID로 조회)
    @Query("SELECT l FROM ListEntity l WHERE l.lid = :id")
    Optional<ListEntity> findBylId(@Param("id") Long id);

    // DELETE 쿼리
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM list_table WHERE lid = :id", nativeQuery = true)
    void deleteBylId(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(value = "UPDATE list_table SET list_like = list_like + 1 WHERE lid = :id", nativeQuery = true)
    void incrementLike(@Param("id") Long id);

    // 좋아요 수 감소
    @Modifying
    @Transactional
    @Query(value = "UPDATE list_table SET list_like = list_like - 1 WHERE lid = :listId", nativeQuery = true)
    void decrementLike(@Param("listId") Long listId);


    @Query(value = "SELECT * FROM list_table ORDER BY list_created_time DESC",
            countQuery = "SELECT COUNT(*) FROM list_table",
            nativeQuery = true)
    Page<ListEntity> findAllWithPaging(Pageable pageable);

    @Query(value = "SELECT * FROM list_table " +
            "WHERE list_title LIKE %:keyword% " +
            "OR list_type LIKE %:keyword% " +
            "OR list_menu LIKE %:keyword% " +
            "OR list_address LIKE %:keyword%",
            countQuery = "SELECT COUNT(*) FROM list_table " +
                    "WHERE list_title LIKE %:keyword% " +
                    "OR list_type LIKE %:keyword% " +
                    "OR list_menu LIKE %:keyword% " +
                    "OR list_address LIKE %:keyword%",
            nativeQuery = true)
    Page<ListEntity> searchAllByKeyword(@Param("keyword") String keyword, Pageable pageable);



}



