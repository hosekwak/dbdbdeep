package com.example.database_project.repository;

import com.example.database_project.entity.MemberEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO member (id, member_name, member_email, member_password) VALUES (?1, ?2, ?3, ?4)", nativeQuery = true)
    void saveMember(Long id, String name, String email, String password);
}