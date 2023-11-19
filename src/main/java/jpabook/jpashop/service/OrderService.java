package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;


    // 엔티티에 핵심 비즈니스 로직을 구현하는 것을 도메인 모델 패턴이라 함 (JPA, ORM)
    // 서비스에 핵심 비즈니스 로직을 구현하는 것을 트랜잭션 스크립트 패턴이라 함 (일반적인 SQL)
    // 두 가지 모두 양립할 수도 있음

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){

        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.fineOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 취소
     * JPA를 사용하는 장점 : 변경 감지가 일어나면서 상태에 따라 Order 및 OrderItem의 값을 변경해줌
     */
    @Transactional
    public void cancelOrder(Long orderId){
        //주문 엔티티 조회
        Order order = orderRepository.fineOne(orderId);
        //주문 취소
        order.cancel();
    }

    /**
     * 검색
     */
    /*public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.fineAll(orderSearch);
    }*/
}
