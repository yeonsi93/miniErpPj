package com.example.minierp.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MaterialDto {

    private Long id;                   /*자재 ID*/

    private String code;               /*자재 코드*/

    private String name;               /*자재 명*/

    private String unit;               /*자재 단위*/

    private Integer stockQuantity;     /*자재 재고수량*/

    @Column(nullable = false)
    private boolean isDeleted = false; /*삭제 여부*/

}
