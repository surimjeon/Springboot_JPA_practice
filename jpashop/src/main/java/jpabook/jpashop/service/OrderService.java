package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.Item.Item;
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
    //주입할 레포지토리들 선언
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    //주문
    @Transactional //주문하려면 어떤 사람이, 어떤 상품을 몇개주문하느지 알아야함
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId); //맴버 id가져옴
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(),count);

        //주문 생성
        Order order = Order.createOrder(member,delivery, orderItem);

        //주문 저장
        orderRepository.save(order);
        //Order (도메인)클래스에서 cascade옵션을 all로 했기 때문에 member와 delivery에 save하지 않아도 관련된 정보 다 저장됨
        return order.getId();
    }

    //주문 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문취소
        order.cancel();

    }
    //주문 검색
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderSearch.findAll(orderSearch);
//    }


}
