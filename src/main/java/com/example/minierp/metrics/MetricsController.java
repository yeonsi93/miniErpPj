package com.example.minierp.metrics;

import com.example.minierp.metrics.MetricsService;
import com.example.minierp.metrics.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
public class MetricsController {

    private final MetricsService metricsService;

    /**
     * 재고 상위 품목 조회
     *
     * @param limit 조회할 개수 (기본값 10)
     * @return 재고량 상위 품목 리스트
     */
    @GetMapping("/top-stock")
    public ResponseEntity<List<TopStockItem>> topStock(@RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(metricsService.topStock(limit));
    }

    /**
     * 일별 입출고 추세 조회
     *
     * @param days 조회할 일 수 (기본값 30)
     * @return 최근 N일간 입고/출고 합계 리스트
     */
    @GetMapping("/history/daily")
    public ResponseEntity<List<DailyHistoryPoint>> daily(@RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(metricsService.dailyHistory(days));
    }

    /**
     * 카테고리별 재고 현황
     *
     * @return 카테고리별 재고 합계 리스트
     */
    @GetMapping("/category/stock")
    public ResponseEntity<List<CategoryStockItem>> categoryStock() {
        return ResponseEntity.ok(metricsService.categoryStock());
    }
}