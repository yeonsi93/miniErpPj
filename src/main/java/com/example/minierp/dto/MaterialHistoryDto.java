package com.example.minierp.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MaterialHistoryDto {

    private Long id;                /* 이력 ID */

    private Long materialId;        /* 자재 ID */

    private String type;            /* 이력 유형 (RECEIVE=입고, RELEASE=출고, UPDATE=수정 등) */

    private int quantity;           /* 변동 수량 */

    private LocalDateTime createdAt; /* 이력 생성 시각 */
}
