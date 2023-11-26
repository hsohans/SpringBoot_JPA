package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item){
        if(item.getId() == null){
            em.persist(item);
        } else {
          // Merge 호출하면 member 넘기면 1차 캐시 엔티티 찾고 없으면 DB에서 조회
          // 1차 캐시 엔티티에 mergeMember 값을 가져와서 member 값갓으로 업데이트
          // 이후 mergerMember 반환
          // 병합시에 입력 받은 값이 없으면 NULL로 처리됨

          em.merge(item);
        }
    }

    public Item fineOne (Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class).getResultList();
    }
}
