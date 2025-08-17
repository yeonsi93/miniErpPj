package com.example.minierp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialReleaseDto {

    private Long materialId;  /*자재 ID*/

    private Integer quantity; /*출고 수량*/

}
