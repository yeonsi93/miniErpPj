package com.example.minierp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialUpdateDto {

    private String name;            /* 자재 명 */

    private String unit;            /* 자재 단위 (EA, BOX, KG 등) */

    private Integer stockQuantity;  /* 자재 재고 수량 */

}
