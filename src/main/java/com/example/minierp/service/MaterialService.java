package com.example.minierp.service;

import com.example.minierp.dto.*;
import com.example.minierp.entity.Material;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MaterialService {

    Long create(MaterialDto dto);

    List<Material> findAll();

    Material findById(Long id);

    void release(MaterialReleaseDto dto);

    void receive(MaterialReceiveDto dto);

    void update(Long id, MaterialUpdateDto dto);

    void delete(Long id);

    List<Material> search(String q, String unit, Integer minStock, Integer maxStock);

    byte[] exportExcel(String q, String unit, Integer minStock, Integer maxStock) throws Exception;
    byte[] exportTemplate() throws Exception;
    UploadReport importExcelUpsert(MultipartFile file, String upsertBy) throws Exception; // 업서트
}
