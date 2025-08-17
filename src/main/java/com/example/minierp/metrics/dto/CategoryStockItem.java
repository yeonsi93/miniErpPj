package com.example.minierp.metrics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 카테고리별 재고 현황 DTO
 * - 재고 통계/대시보드용
 */
@Data
@AllArgsConstructor
public class CategoryStockItem {

    private String category;  /* 카테고리명 */

    private long stock;       /* 해당 카테고리 총 재고 수량 */

}
