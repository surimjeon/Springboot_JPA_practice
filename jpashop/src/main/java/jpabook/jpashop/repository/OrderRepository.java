package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order); //주문을 저장
    }

    public Order findOne(Long id) {
        return em.find(Order.class,id);
    }
    //주문 내역에서의 검색기능 개발은 동적 쿼리로 생성(나중에 하겠다)
//    public List<Order> findAll(OrderSearch orderSearch) {
//        //검색조건에 동적으로 쿼리를 생성
//
//    }


}
