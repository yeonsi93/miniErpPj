package com.example.minierp.metrics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 일별 자재 입출고 현황 DTO
 * - 통계/차트 데이터로 활용
 */
@Data
@AllArgsConstructor
public class DailyHistoryPoint {

    private String date;   /* 기준일 */

    private int receive;   /* 해당 일자의 입고 합계 */

    private int release;   /* 해당 일자의 출고 합계 */

}
