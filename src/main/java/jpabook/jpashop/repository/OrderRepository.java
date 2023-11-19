package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order fineOne(Long id){
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch){

        // jpql로 풀어서 쓸 경우, 너무 복잡하고 지저분함
        /**
         * 권장 X
         */
        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        // 주문 상태 검색
        if (orderSearch.getOrderStatus() != null){
            if(isFirstCondition){
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        // 회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())){
            if(isFirstCondition){
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000);

        if (orderSearch.getOrderStatus() != null){
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())){
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();

        /*return em.createQuery(jpql +
                        " where o.status = :status " +
                        " and m.name like :name ", Order.class)
                .setMaxResults(1000) // 최대 1000건
                .getResultList();*/

        /*return em.createQuery("select o from Order o join o.member m" +
                        " where o.status = :status " +
                        " and m.name like :name ", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                //.setFirstResult()  // 페이징 처리
                .setMaxResults(1000) // 최대 1000건
                .getResultList();*/

    }

    /**
     * JPA Criteria 표준이지만 권장 X
     * @param orderSearch
     * @return
     */
    public List<Order> findAllByCriteria(OrderSearch orderSearch){
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        // 주문 상태 검색
        if(orderSearch.getOrderStatus() != null){
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }
        // 회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())){
            Predicate name = cb.like(m.get("name"), "%" + orderSearch.getMemberName() + "%" );
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }

    /**
     * Query DSL 로 작성해야 함
     * 실무 : Spring Boot + JPA + Query DSL 꼭 사용
     */
}
