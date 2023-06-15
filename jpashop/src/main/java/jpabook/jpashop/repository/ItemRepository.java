package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor //다른 객체 생성자 주입을 위해(여기선 엔티티메니저)
public class ItemRepository {
    private final EntityManager em;

    //상품 저장
    public void save(Item item) {
        if (item.getId() ==null) { //없는 아이템이면 저장
            em.persist(item);
        }
        else {
            em.merge(item); //있다면 merge(update와 유사)
        }
    }
    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i",Item.class)
                .getResultList();
    }
}
