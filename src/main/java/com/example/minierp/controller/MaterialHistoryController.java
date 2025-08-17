package com.example.minierp.controller;

import com.example.minierp.dto.MaterialHistoryDto;
import com.example.minierp.service.MaterialHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialHistoryController {

    private final MaterialHistoryService materialHistoryService;

    /**
     * 특정 자재의 이력 조회
     *
     * @param materialId 자재 ID
     * @return 자재 이력 목록
     */
    @GetMapping("/{materialId}/history")
    public List<MaterialHistoryDto> getHistory(@PathVariable Long materialId) {
        return materialHistoryService.getHistoryByMaterialId(materialId);
    }
}
