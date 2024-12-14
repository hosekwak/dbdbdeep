package com.example.database_project.repository;

import com.example.database_project.entity.ListFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListFileRepository extends JpaRepository<ListFileEntity,Long> {
}
