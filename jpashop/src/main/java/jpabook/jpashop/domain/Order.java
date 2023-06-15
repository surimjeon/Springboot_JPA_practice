package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders") //name을 orders로 바꿔주기 위해
public class Order {

    @Id @GeneratedValue //기본키를 자동생성해주는 어노테이션(함께 쓰기)
    @Column(name="order_id")
    private Long id;

    //양방향 참조 시 둘다 값을 바꿔버리면 안되니까
    @ManyToOne(fetch = FetchType.LAZY) //내 관계가 앞으로
    @JoinColumn(name="member_id") //join을 외래키에서 걸어주기
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    //cascade: 관계에 있는 모든 엔티티를 같이 persist해줌
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태 [order, cancel]

    //연관관계 메서드(양방향 관계에 있는 객체를 연결하는 메서드)
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this); //멤버의 주문목록 list에 현재주문(this)를 추가함
    }
    //orderitem
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem); //orderItems리스트에 orderItem객체 추가
        orderItem.setOrder(this); //현재의 주문을 할당
    }
    //delivery
    public void setDelivery(Delivery delivery) {
        this.delivery=delivery; //매개변수로 받은 delivery에(delivery타입) 현재주문(this)를 할당
        delivery.setOrder(this);
    }

    //생성메서드(주문 생성) : 생성할 때부터 createorder해서 주문생성에 대한걸 완성을 시킴 //
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) { //...문법으로 여러개를 리스트로 넘김
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        // 주문에 주문한 아이템을 1개씩 추가
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        //첫상태는 order상태
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;

    }

    //비즈니스 로직(setter대신) //
    //취소 - 생성 매서드
    public void cancel() {
        //배송완료된 상품이라면
        if(delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소 불가능");
        }
        //배송완료안됐다면 취소가능
        this.setStatus(OrderStatus.CANCEL);
        for(OrderItem orderItem : this.orderItems) {
            orderItem.cancel(); //고객이 1개 주문취소할 떄 상품 각각 취소해야하므로, orderitem에 메소드 정의
        }
    }
    //주문의 전체가격 조회 로직
    public int getTotalPrice() {
        int totalPrice =0;
        for(OrderItem orderItem : orderItems) {
            totalPrice+=orderItem.getTotalPrice();
            //주문 시 주문가격*수량을 해야하므로, orderItem
            //ex)책 3권, 영화 2매 -> 각 주문은 orderitem에서 계산하고 넘겨줌
        }
        return totalPrice;
    }


}
