package com.example.minierp.metrics;

import com.example.minierp.metrics.dto.*;
import java.util.List;

public interface MetricsService {
    List<TopStockItem> topStock(int limit);
    List<DailyHistoryPoint> dailyHistory(int days);
    List<CategoryStockItem> categoryStock();          // 카테고리별 합계
}