package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, Book param){
        // findItem은 DB에서 찾아온 영속성 엔티티
        // 그래서, DB 호출을 하지 않아도 자동으로 쿼리 만들어서 업데이트 함
        // 보통 이 방법을 사용함
        Item findItem = itemRepository.fineOne(itemId);
        // findItem.change(price, name, stockQuantity);
        findItem.setPrice(param.getPrice());
        findItem.setName(param.getName());
        findItem.setStockQuantity(param.getStockQuantity());
        // itemRepository.save(findItem);

    }

    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity){
        // findItem은 DB에서 찾아온 영속성 엔티티
        // 그래서, DB 호출을 하지 않아도 자동으로 쿼리 만들어서 업데이트 함
        // 보통 이 방법을 사용함
        Item findItem = itemRepository.fineOne(itemId);
        // findItem.change(price, name, stockQuantity);
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
        // itemRepository.save(findItem);

    }

    // 서비스 계층에 식별자 아이디와 데이터를 명학하게 전핟해야함
    @Transactional
    public void updateItem(Long itemId, UpdateItemDto itemDto){
        // findItem은 DB에서 찾아온 영속성 엔티티
        // 그래서, DB 호출을 하지 않아도 자동으로 쿼리 만들어서 업데이트 함
        // 보통 이 방법을 사용함
        Item findItem = itemRepository.fineOne(itemId);
        // findItem.change(price, name, stockQuantity);
        findItem.setPrice(itemDto.getPrice());
        findItem.setName(itemDto.getName());
        findItem.setStockQuantity(itemDto.getStockQuantity());
        // itemRepository.save(findItem);

    }


    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemid){
        return itemRepository.fineOne(itemid);
    }
}
