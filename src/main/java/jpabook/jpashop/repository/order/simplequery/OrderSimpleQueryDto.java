package jpabook.jpashop.repository.order.simplequery;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class OrderSimpleQueryDto {

        private Long orderId; // 주문 아이디
        private String name;  // 주문자
        private LocalDateTime orderDate; //주문시간
        private OrderStatus orderStatus; //주문상태
        private Address address; // 배송지정보

        public OrderSimpleQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
            this.orderId = orderId;
            this.name = name; //LAZY가 초기화
            this.orderDate = orderDate;
            this.orderStatus = orderStatus;
            this.address = address; //LAZY 초기화
        }

}
