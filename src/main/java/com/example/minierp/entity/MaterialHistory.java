package com.example.minierp.entity;

import com.example.minierp.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "material_history",
        indexes = {
                @Index(name = "idx_history_material_created", columnList = "material_id, createdAt"),
                @Index(name = "idx_history_type_created",     columnList = "type, createdAt")
        })
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MaterialHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   /* 이력 ID */

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id")
    private Material material;  /* 연관된 자재 */

    @Enumerated(EnumType.STRING)
    private TransactionType type;  /* 거래 유형 (RECEIVE = 입고, RELEASE = 출고) */

    private Integer quantity;      /* 변동 수량 */

    private LocalDateTime createdAt; /* 이력 생성 일시 */

}