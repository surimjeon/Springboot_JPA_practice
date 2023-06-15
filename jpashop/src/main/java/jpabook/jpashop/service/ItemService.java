package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    //상품 서비스클래스는 상품리포지토리에 단순위임만 하고 있음
    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }
    public List<Item> findItems() {
        return itemRepository.findAll();
    }
    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
