package com.example.minierp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MaterialReceiveDto {

    private Long materialId;   /* 자재 ID */

    private Integer quantity;  /* 입고 수량 */

}
