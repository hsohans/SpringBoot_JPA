package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * xToOne (ManyToOne, OneToOne)
 * Order
 * Order -> Member
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order:all){
            order.getMember().getName(); // Lazy 강제 초기화
            order.getDelivery().getAddress(); // Lazy 강제 초기화
        }
        return all;
    }

    /**
     * Order -> SQL 1번 실행 -> 결과 주문 Rows 2개
     * @return
     */
    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        // ORDER 2개
        // N + 1 -> 1 + 회원 N + 배송 N
        // N + 1 -> 1(첫번째 쿼리) + N(N 개의 추가 쿼리) - 회원 N, 배송 N
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());

        // 2개
        // 첫번째, ORDER의 멤버를 찾아야 하므로 멤버 쿼리가 나감
        // 주문을 조회해야하므로 DELIVERY 정보도 조회

        // 두번째, ORDER의 멤버를 찾아야 하므로 멤버 쿼리가 나감
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    /**
     * 회원과 배송이 많아짐에 따라 느려질 수 있는 상황을 Fetch Join을 통해 해결
     * SQL에 FETCH 명시하여 사용 (JPA에서만 사용할 수 있음)
     * 100% FETCH를 정확하게 이해하고선 사용해야 함.
     * @return
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    /**
     * 두팀이 협의를 해서 아래 DTO를 명세함
     */
    @Data
    static class SimpleOrderDto{
        private Long orderId; // 주문 아이디
        private String name;  // 주문자
        private LocalDateTime orderDate; //주문시간
        private OrderStatus orderStatus; //주문상태
        private Address address; // 배송지정보

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //LAZY가 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //LAZY 초기화
        }
    }

}
