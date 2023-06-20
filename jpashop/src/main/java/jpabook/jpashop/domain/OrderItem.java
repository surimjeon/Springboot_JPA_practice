package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jpabook.jpashop.domain.Item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//이렇게 해놓으면 다른 클래스에서 orderitem객체를 새로 생성하지 못하게 해서
// 주문을 새로 생성할 때, 일관되게 createOrder을 써서 생성할 수 있도록 함
public class OrderItem {

    @Id @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY) //하나의 order는 여러개의 orderitem을 가질 수 있음
    @JoinColumn(name="order_id")
    private Order order;

    private int orderPrice; //주문가격
    private int count; //주문 수량

    //생성 메서드//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem(); //새 객체 만들기
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count); //orderitem을 생성하면서 각 item에서는 개수를 줄인다
        return orderItem;
    }

    //비즈니스 로직//
    public void cancel() {
        getItem().addStock(count); //재고수량을 원복
    }

    public int getTotalPrice() {
        return getOrderPrice()*getCount();
    }
}
