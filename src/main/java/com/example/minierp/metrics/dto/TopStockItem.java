package com.example.minierp.metrics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 재고 상위 품목 DTO
 * - 재고량이 많은 상위 N개 품목 조회용
 */
@Data
@AllArgsConstructor
public class TopStockItem {

    private Long id;              /* 자재 ID */

    private String code;          /* 자재 코드 */

    private String name;          /* 자재명 */

    private String category;      /* 카테고리 */

    private int stockQuantity;    /* 현재 재고 수량 */

}
