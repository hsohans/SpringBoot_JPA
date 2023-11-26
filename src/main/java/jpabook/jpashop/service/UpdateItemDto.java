package jpabook.jpashop.service;


import lombok.Getter;
import lombok.Setter;


// 파라미터 많을 경우 만들어서 사용함
@Getter @Setter
public class UpdateItemDto {
    private int price;
    private String name;
    private int stockQuantity;
}
