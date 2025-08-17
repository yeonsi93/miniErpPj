package com.example.minierp.service.impl;

import com.example.minierp.dto.*;
import com.example.minierp.entity.Material;
import com.example.minierp.entity.MaterialHistory;
import com.example.minierp.enums.TransactionType;
import com.example.minierp.repository.MaterialHistoryRepository;
import com.example.minierp.repository.MaterialRepository;
import com.example.minierp.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepository materialRepository;
    private final MaterialHistoryRepository materialHistoryRepository;

    @Override
    @Transactional
    public Long create(MaterialDto dto) {
        if (materialRepository.existsByCode(dto.getCode())) {
            throw new IllegalArgumentException("이미 존재하는 자재 코드입니다.");
        }

        Material material = Material.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .unit(dto.getUnit())
                .stockQuantity(dto.getStockQuantity() == null ? 0 : dto.getStockQuantity())
                .category(null)
                .build();

        return materialRepository.save(material).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Material> findAll() {
        return materialRepository.findAll().stream()
                .filter(m -> !m.isDeleted())
                .toList();
    }

    @Override
    public Material findById(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("자재를 찾을 수 없습니다. id=" + id));
    }

    @Override
    @Transactional
    public void release(MaterialReleaseDto dto) {
        Material material = materialRepository.findById(dto.getMaterialId())
                .orElseThrow(() -> new IllegalArgumentException("자재가 존재하지 않습니다."));

        int newQuantity = material.getStockQuantity() - dto.getQuantity();
        if (newQuantity < 0) {
            throw new IllegalArgumentException("재고가 부족합니다.");
        }

        material.setStockQuantity(newQuantity);
        materialRepository.save(material);

        materialHistoryRepository.save(MaterialHistory.builder()
                .material(material)
                .type(TransactionType.RELEASE)
                .quantity(dto.getQuantity())
                .createdAt(LocalDateTime.now())
                .build());

    }

    @Override
    @Transactional
    public void receive(MaterialReceiveDto dto) {
        Material material = materialRepository.findById(dto.getMaterialId())
                .orElseThrow(() -> new IllegalArgumentException("자재가 존재하지 않습니다."));

        int newQuantity = material.getStockQuantity() + dto.getQuantity();
        material.setStockQuantity(newQuantity);

        materialRepository.save(material);

        materialHistoryRepository.save(MaterialHistory.builder()
                .material(material)
                .type(TransactionType.RECEIVE)
                .quantity(dto.getQuantity())
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Override
    @Transactional
    public void update(Long id, MaterialUpdateDto dto) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("자재가 존재하지 않습니다."));

        material.setName(dto.getName());
        material.setUnit(dto.getUnit());
        material.setStockQuantity(dto.getStockQuantity());

        materialRepository.save(material);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("자재가 존재하지 않습니다."));

        material.setDeleted(true);
        materialRepository.save(material);
    }

    @Override
    public List<Material> search(String q, String unit, Integer minStock, Integer maxStock) {
        return materialRepository.search(
                (q == null || q.isBlank()) ? null : q,
                (unit == null || unit.isBlank()) ? null : unit,
                minStock, maxStock
        );
    }

    @Override
    public byte[] exportExcel(String q, String unit, Integer minStock, Integer maxStock) throws Exception {
        List<Material> list = search(q, unit, minStock, maxStock);

        try (var wb = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
            var sh = wb.createSheet("materials");
            int r = 0;
            String[] cols = {"ID","CODE","NAME","UNIT","STOCK","CREATED_AT"};
            var head = sh.createRow(r++);
            for (int i=0;i<cols.length;i++) head.createCell(i).setCellValue(cols[i]);

            for (var m : list) {
                var row = sh.createRow(r++);
                row.createCell(0).setCellValue(m.getId());
                row.createCell(1).setCellValue(nvl(m.getCode()));
                row.createCell(2).setCellValue(nvl(m.getName()));
                row.createCell(3).setCellValue(nvl(m.getUnit()));
                row.createCell(4).setCellValue(m.getStockQuantity());
                row.createCell(5).setCellValue(m.getCreatedAt() == null ? "" : m.getCreatedAt().toString());
            }
            for (int i=0;i<cols.length;i++) sh.autoSizeColumn(i);

            try (var bos = new java.io.ByteArrayOutputStream()) {
                wb.write(bos);
                return bos.toByteArray();
            }
        }
    }

    private static String nvl(String s){ return s==null?"":s; }

    private static String getString(org.apache.poi.ss.usermodel.Row row, int c){
        var cell = row.getCell(c);
        if (cell == null) return null;
        cell.setCellType(org.apache.poi.ss.usermodel.CellType.STRING);
        String v = cell.getStringCellValue();
        return (v != null && !v.isBlank()) ? v.trim() : null;
    }

    private static Integer getInteger(org.apache.poi.ss.usermodel.Row row, int c){
        var cell = row.getCell(c);
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case NUMERIC: return (int) cell.getNumericCellValue();
            case STRING:
                try { return Integer.parseInt(cell.getStringCellValue().trim()); } catch(Exception e){ return null; }
            default: return null;
        }
    }

    @Override
    @Transactional
    public UploadReport importExcelUpsert(MultipartFile file, String upsertBy) throws Exception {
        int inserted = 0, updated = 0, skipped = 0;
        var errors = new java.util.ArrayList<String>();

        try (var wb = new XSSFWorkbook(file.getInputStream())) {
            var sheet = wb.getSheetAt(0);
            if (sheet == null) throw new IllegalArgumentException("시트를 찾을 수 없습니다.");

            var header = sheet.getRow(0);
            if (header == null) throw new IllegalArgumentException("헤더 행이 없습니다.");

            int idxId=-1, idxCode=-1, idxName=-1, idxUnit=-1, idxStock=-1, idxCategory=-1, idxSafety=-1;

            for (int c = 0; c < header.getLastCellNum(); c++) {
                String h = getString(header, c);
                if (h == null) continue;
                String key = h.trim().toUpperCase();

                switch (key) {
                    case "ID" -> idxId = c;
                    case "CODE" -> idxCode = c;
                    case "NAME" -> idxName = c;
                    case "UNIT" -> idxUnit = c;
                    case "STOCK" -> idxStock = c;
                    case "CATEGORY" -> idxCategory = c;
                    case "SAFETY_STOCK", "SAFETYSTOCK", "SAFETY" -> idxSafety = c;
                }
            }

            if (idxCode < 0) throw new IllegalArgumentException("헤더에 CODE 컬럼이 없습니다.");

            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                var row = sheet.getRow(r);
                if (row == null) continue;

                String code      = (idxCode     >=0) ? getString(row, idxCode)     : null;
                String name      = (idxName     >=0) ? getString(row, idxName)     : null;
                String unit      = (idxUnit     >=0) ? getString(row, idxUnit)     : null;
                Integer stock    = (idxStock    >=0) ? getInteger(row, idxStock)   : null;
                String category  = (idxCategory >=0) ? getString(row, idxCategory) : null;
                Integer safety   = (idxSafety   >=0) ? getInteger(row, idxSafety)  : null;

                if (code == null || code.isBlank()) { skipped++; continue; }

                try {
                    var materialOpt = materialRepository.findByCode(code);
                    if (materialOpt.isEmpty()) {
                        // 신규
                        Material m = Material.builder()
                                .code(code)
                                .name(name != null ? name : code)
                                .unit(unit)
                                .stockQuantity(stock != null ? stock : 0)
                                .category(category)
                                .build();
                        materialRepository.save(m);
                        inserted++;
                    } else {
                        // 수정
                        Material m = materialOpt.get();
                        if (name != null)   m.setName(name);
                        if (unit != null)   m.setUnit(unit);
                        if (stock != null)  m.setStockQuantity(stock);
                        if (category != null) m.setCategory(category);
                        updated++;
                    }
                } catch (Exception e) {
                    errors.add("Row " + (r+1) + ": " + e.getMessage());
                }
            }
        }

        return new UploadReport(inserted, updated, skipped, errors);
    }


    @Override
    public byte[] exportTemplate() throws Exception {
        try (var wb = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
            var sh = wb.createSheet("materials");
            int r = 0;

            String[] headers = {"CODE","NAME","UNIT","STOCK","CATEGORY","SAFETY_STOCK"};
            var head = sh.createRow(r++);
            for (int i=0;i<headers.length;i++) head.createCell(i).setCellValue(headers[i]);

            sh.createRow(r++).setRowNum(r-1);
            sh.getRow(r-1).createCell(0).setCellValue("MAT-001");
            sh.getRow(r-1).createCell(1).setCellValue("A4 복사용지");
            sh.getRow(r-1).createCell(2).setCellValue("박스");
            sh.getRow(r-1).createCell(3).setCellValue(100);
            sh.getRow(r-1).createCell(4).setCellValue("문구류");
            sh.getRow(r-1).createCell(5).setCellValue(0);

            sh.createRow(r++).setRowNum(r-1);
            sh.getRow(r-1).createCell(0).setCellValue("MAT-002");
            sh.getRow(r-1).createCell(1).setCellValue("볼펜(파란색)");
            sh.getRow(r-1).createCell(2).setCellValue("개");
            sh.getRow(r-1).createCell(3).setCellValue(250);
            sh.getRow(r-1).createCell(4).setCellValue("문구류");
            sh.getRow(r-1).createCell(5).setCellValue(0);

            for (int i=0;i<headers.length;i++) sh.autoSizeColumn(i);

            try (var bos = new java.io.ByteArrayOutputStream()) {
                wb.write(bos);
                return bos.toByteArray();
            }
        }
    }

}
