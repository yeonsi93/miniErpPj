package com.example.minierp.service;

import com.example.minierp.dto.MaterialHistoryDto;

import java.util.List;

public interface MaterialHistoryService {
    List<MaterialHistoryDto> getHistoryByMaterialId(Long materialId);
}
