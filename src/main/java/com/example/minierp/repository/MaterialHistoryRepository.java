package com.example.minierp.repository;

import com.example.minierp.entity.MaterialHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MaterialHistoryRepository extends JpaRepository<MaterialHistory, Long> {
    List<MaterialHistory> findByMaterialIdOrderByCreatedAtDesc(Long materialId);

    List<MaterialHistory> findByCreatedAtBetween(LocalDateTime from, LocalDateTime to);
}
