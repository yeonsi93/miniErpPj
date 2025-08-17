package com.example.minierp.service.impl;

import com.example.minierp.dto.MaterialHistoryDto;
import com.example.minierp.entity.MaterialHistory;
import com.example.minierp.repository.MaterialHistoryRepository;
import com.example.minierp.service.MaterialHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MaterialHistoryServiceImpl implements MaterialHistoryService {

    private final MaterialHistoryRepository historyRepository;

    @Override
    public List<MaterialHistoryDto> getHistoryByMaterialId(Long materialId) {
        List<MaterialHistory> historyList = historyRepository.findByMaterialIdOrderByCreatedAtDesc(materialId);

        return historyList.stream().map(h -> {
            MaterialHistoryDto dto = new MaterialHistoryDto();
            dto.setId(h.getId());
            dto.setMaterialId(h.getMaterial().getId());
            dto.setType(h.getType() != null ? h.getType().name() : null);
            dto.setQuantity(h.getQuantity());
            dto.setCreatedAt(h.getCreatedAt());
            return dto;
        }).collect(Collectors.toList());
    }
}
