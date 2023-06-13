package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
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




}
