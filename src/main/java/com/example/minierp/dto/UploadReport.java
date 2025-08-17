package com.example.minierp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class UploadReport {

    private int inserted;          /* 신규 등록된 자재 수 */

    private int updated;           /* 수정된 자재 수 */

    private int skipped;           /* 건너뛴 자재 수 (중복/조건 불충족 등) */

    private List<String> errors;   /* 오류 메시지 목록 */

    public UploadReport() {
        this.errors = new ArrayList<>();
    }

    public void addError(String msg) {
        this.errors.add(msg);
    }
}
