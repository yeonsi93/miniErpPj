package com.example.minierp.controller;

import com.example.minierp.dto.*;
import com.example.minierp.entity.Material;
import com.example.minierp.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    /**
     * 자재 등록
     *
     * @param dto 자재 등록 요청 DTO
     * @return 생성된 자재 ID
     */
    @PostMapping
    public ResponseEntity<Long> createMaterial(@RequestBody MaterialDto dto) {
        Long id = materialService.create(dto);
        return ResponseEntity.ok(id);
    }

    /**
     * 자재 전체 조회
     *
     * @return 전체 자재 목록
     */
    @GetMapping
    public ResponseEntity<List<Material>> getAllMaterials() {
        List<Material> materials = materialService.findAll();
        return ResponseEntity.ok(materials);
    }

    /**
     * 자재 출고 처리
     *
     * @param dto 출고 요청 DTO
     * @return "출고 완료" 메시지
     */
    @PostMapping("/release")
    public ResponseEntity<String> release(@RequestBody MaterialReleaseDto dto) {
        materialService.release(dto);
        return ResponseEntity.ok("출고 완료");
    }

    /**
     * 자재 입고 처리
     *
     * @param dto 입고 요청 DTO
     * @return "입고 완료" 메시지
     */
    @PostMapping("/receive")
    public ResponseEntity<String> receive(@RequestBody MaterialReceiveDto dto) {
        materialService.receive(dto);
        return ResponseEntity.ok("입고 완료");
    }

    /**
     * 자재 수정
     *
     * @param id  수정할 자재 ID
     * @param dto 수정 요청 DTO
     * @return "자재 수정 완료" 메시지
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id,
                                         @RequestBody MaterialUpdateDto dto) {
        materialService.update(id, dto);
        return ResponseEntity.ok("자재 수정 완료");
    }

    /**
     * 자재 삭제 (사용안함 처리)
     *
     * @param id 자재 ID
     * @return "자재 삭제 완료" 메시지
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        materialService.delete(id);
        return ResponseEntity.ok("자재 삭제 완료");
    }

    /**
     * 자재 검색/필터링
     *
     * @param q        코드/이름 키워드 검색
     * @param unit     단위 필터
     * @param minStock 최소 수량
     * @param maxStock 최대 수량
     * @return 조건에 맞는 자재 목록
     */
    @GetMapping("/search")
    public ResponseEntity<List<Material>> search(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String unit,
            @RequestParam(required = false) Integer minStock,
            @RequestParam(required = false) Integer maxStock) {
        return ResponseEntity.ok(materialService.search(q, unit, minStock, maxStock));
    }

    /**
     * 자재 목록 엑셀 다운로드 (검색/필터 조건 반영)
     *
     * @param q        코드/이름 키워드 검색
     * @param unit     단위 필터
     * @param minStock 최소 수량
     * @param maxStock 최대 수량
     * @return Excel 파일 (materials.xlsx)
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportExcel(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String unit,
            @RequestParam(required = false) Integer minStock,
            @RequestParam(required = false) Integer maxStock) throws Exception {
        byte[] bytes = materialService.exportExcel(q, unit, minStock, maxStock);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=materials.xlsx")
                .header("Content-Type","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(bytes);
    }

    /**
     * 자재 업로드용 Excel 템플릿 다운로드
     * (CODE, NAME, UNIT, STOCK, CATEGORY 컬럼 포함)
     *
     * @return Excel 템플릿 파일
     */
    @GetMapping("/template")
    public ResponseEntity<byte[]> downloadTemplate() throws Exception {
        byte[] bytes = materialService.exportTemplate();
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=materials_upload_template.xlsx")
                .header("Content-Type","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(bytes);
    }

    /**
     * 자재 Excel 업로드
     *
     * @param file     업로드할 Excel 파일
     * @param upsertBy 업서트 기준 ("code" | "id")
     * @return 업로드 결과
     */
    @PostMapping("/upload")
    public ResponseEntity<UploadReport> uploadExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam(name = "upsertBy", defaultValue = "code") String upsertBy
    ) throws Exception {
        UploadReport report = materialService.importExcelUpsert(file, upsertBy);
        return ResponseEntity.ok(report);
    }
}
