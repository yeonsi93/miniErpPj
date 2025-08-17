package com.example.minierp.metrics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 재고 임계치(안전재고) 현황 요약 DTO
 * - 안전재고 기준 미만/이상 품목 수를 요약
 */
@Data
@AllArgsConstructor
public class LowStockSummary {

    private int lowCount;  /* 안전재고 미만 품목 수 */

    private int okCount;   /* 안전재고 이상 품목 수 */

}
