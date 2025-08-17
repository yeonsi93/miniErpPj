package com.example.minierp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 자재(Material) 엔티티
 * - 자재 코드, 이름, 단위, 재고, 카테고리, 안전재고 등 관리
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                /* 자재 ID */

    @Column(nullable = false, unique = true)
    private String code;            /* 자재 코드 (예: M-0001) */

    @Column(nullable = false)
    private String name;            /* 자재명 (예: 나사, 볼트 등) */

    private String unit;            /* 단위 (예: EA, BOX, KG 등) */

    private int stockQuantity;      /* 현재 재고 수량 */

    private LocalDateTime createdAt; /* 생성 일시 */

    @Column(nullable = false)
    private boolean deleted = false; /* 삭제 여부 (true = 삭제 처리됨) */

    @Column(length = 30)
    private String category;        /* 카테고리 (예: 사무용품, 소모품, 포장재 등) */

    @PrePersist
    public void setCreatedAt() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
