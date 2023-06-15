package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Item.Book;
import jpabook.jpashop.domain.Item.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {
    //1. 상품 주문 성공하는지
    //2. 재고 수량 초과하면 안됨
    //3. 주문 취소가 성공해야함

    @PersistenceContext
    EntityManager em;

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;


    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember(); //맴버와 상품 만들기(다른 메소드에서도 쓰기 위해)
        Item item = createBook("시골 JPA", 10000, 10); //이름, 가격, 재고
        int orderCount =2; //2권 주문

        //when
        //service에서 정의한 주문 메서드
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품주문식 상태는 order다", OrderStatus.ORDER, getOrder.getStatus()); //기대값, 실제값
        assertEquals("주문한 상품 종류수 정확해야함", 1, getOrder.getOrderItems().size());
        assertEquals("주문가격은 가격*수량임", 10000*2, getOrder.getTotalPrice());
        assertEquals("상품 수만큼 재고가 줄어야함", 8, item.getStockQuantity());
    }

    @Test(expected = NotEnoughStockException.class) //예외처리 반영
    public void 재고수량초과() throws Exception {
        //given
        Member member = createMember();
        Item item =createBook("시골 JPA", 10000, 10);
        int orderCount= 11; // 재고는 10권인데, 11권 주문
        //when
        orderService.order(member.getId(), item.getId(), orderCount);
        //then
        fail("재고수량부족 예외가 발생");
    }

    @Test
    public void 주문취소() throws Exception{
        //주문을 취소하면 재고가 증가해야함
        //given 먼저 주문을 하고
        Member member = createMember();
        Item item = createBook("시골 JPA", 10000, 10);
        int orderCount =2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when 취소
        orderService.cancelOrder(orderId);
        //then 검증
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals("주문 취소 상태는 cancel", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문 취소상품은 그만큼 재고 증가", 10, item.getStockQuantity());

    }

    private Member createMember() { //맴버는 같은 맴버로 설정
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","경기","123-123"));
        em.persist(member);
        return member;
    }

    //item을 다르게 해서 테스트
    private Book createBook(String name, int price, int stockQuntitiy) {
        Book book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuntitiy);
        book.setPrice(price);
        em.persist(book);
        return book;
    }

}