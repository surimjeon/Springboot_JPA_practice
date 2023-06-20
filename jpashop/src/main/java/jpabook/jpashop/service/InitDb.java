package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.Item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init() { //init시점
        initService.dbinit1(); //사용자1
        initService.dbinit2(); //사용자2
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService { //서비스클래스 하나 만들기
        private final EntityManager em; //샘플데이터만들거니까 엔티티 메니저

        public void dbinit1() {
            Member member = createMember("userA","서울","1","1111");
            em.persist(member); //맴버 생성

            Book book1 = createBook("JPA1 BOOK",10000, 100);
            em.persist(book1); //2권의 책 생성

            Book book2 = createBook("JPA2 BOOK",20000, 100);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000,1); //주문 생성
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000,2);
            Order order = Order.createOrder(member, createDelivery(member), orderItem1,orderItem2);

            em.persist(order);
        }

        public void dbinit2() {
            Member member = createMember("userB","부산","2","2222");
            em.persist(member);

            Book book1 = createBook("SPRING1 BOOK",20000, 200);
            em.persist(book1);

            Book book2 = createBook("SPRING2 BOOK",40000, 300);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000,3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000,4);
            Order order = Order.createOrder(member, createDelivery(member), orderItem1,orderItem2);

            em.persist(order);
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress()); //회원의 배송정보 넘기기
            return delivery;
        }

        private Book createBook(String name, int price, int stockQuantity) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);
            return book;
        }

        private Member createMember(String name, String city, String street, String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

    }
}