package com.example.minierp.metrics;

import com.example.minierp.entity.Material;
import com.example.minierp.entity.MaterialHistory;
import com.example.minierp.metrics.dto.*;
import com.example.minierp.repository.MaterialHistoryRepository;
import com.example.minierp.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MetricsServiceImpl implements MetricsService {

    private final MaterialRepository materialRepository;
    private final MaterialHistoryRepository historyRepository;

    @Override
    public List<TopStockItem> topStock(int limit) {
        var page = materialRepository.findByDeletedFalse(
                PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "stockQuantity"))
        );
        return page.stream()
                .map(m -> new TopStockItem(
                        m.getId(), m.getCode(), m.getName(),
                        nullTo(m.getCategory(), "미지정"),
                        m.getStockQuantity()))
                .collect(Collectors.toList());
    }

    @Override
    public List<DailyHistoryPoint> dailyHistory(int days) {
        LocalDate today = LocalDate.now();
        LocalDate fromDate = today.minusDays(days - 1); // inclusive
        LocalDateTime from = fromDate.atStartOfDay();
        LocalDateTime to = today.plusDays(1).atStartOfDay(); // exclusive

        List<MaterialHistory> logs = historyRepository.findByCreatedAtBetween(from, to);

        // date -> [receive, release]
        Map<LocalDate, int[]> acc = new LinkedHashMap<>();
        for (int i=0;i<days;i++) acc.put(fromDate.plusDays(i), new int[]{0,0});

        for (MaterialHistory h : logs) {
            LocalDate d = h.getCreatedAt().toLocalDate();
            int[] v = acc.get(d);
            if (v == null) continue;
            String t = h.getType().toString(); // enum/String 모두 대응
            if ("RECEIVE".equalsIgnoreCase(t)) v[0] += safe(h.getQuantity());
            else if ("RELEASE".equalsIgnoreCase(t)) v[1] += safe(h.getQuantity());
        }

        return acc.entrySet().stream()
                .map(e -> new DailyHistoryPoint(e.getKey().toString(), e.getValue()[0], e.getValue()[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryStockItem> categoryStock() {
        return materialRepository.sumStockByCategory().stream()
                .map(r -> new CategoryStockItem(nullTo(r.getCategory(), "미지정"),
                        r.getTotal() == null ? 0 : r.getTotal()))
                .collect(Collectors.toList());
    }

    private static int safe(Integer n){ return n==null?0:n; }
    private static String nullTo(String s, String d){ return (s==null || s.isBlank()) ? d : s; }
}