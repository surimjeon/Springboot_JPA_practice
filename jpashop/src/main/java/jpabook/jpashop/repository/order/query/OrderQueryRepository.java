package jpabook.jpashop.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {
    //컬렉션은 별도로 조회,
    //query: 루트 1번, 컬렉션 n번->단건 조회에서 많이 사용하는 방식

    private final EntityManager em;
    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders();//1번쿼리 날리면

        result.forEach(o->{
            //orderqueryDto에서 정의하지 않은 n의 orderitems은 따로 findOrderItems를 써서 찾아줌
            // n은 루프를 돌면서 직접 데이터를 채움(set으로)=>n번의 쿼리
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    //1:n관계인 orderitems조회
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery("select new" +
                "jpabook.jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name, " +
                "oi.orderPrice, oi.count)" +
                " from OrderItem oi" +
                " join oi.item i" +
                " where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId",orderId)
                .getResultList();

    }

    //1:n관계(컬렉션)을 제외한 나머지를 한번에 조회
    private List<OrderQueryDto> findOrders() {
        return em.createQuery("select new jpabook.jpashop.repository.order.query." +
                        "OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders();//n관계(컬렉션)을 제외한 나머지(1)를 한번에 조회
        //여기서(1) 얻은 식별자 orderId로 orderItem(n)을 Map자료구조로 한방에 조회
        Map<Long, List<OrderItemQueryDto>> orderItemMap =
                findOrderItemMap(toOrderIds(result));
        // 루프 돌면서 컬렉션 추가(추가 쿼리 실행x)
        result.forEach(o->o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }

    private List<Long> toOrderIds(List<OrderQueryDto> result) {
        return result.stream().map(o->o.getOrderId()).collect(Collectors.toList());
    }

    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery("select new jpabook" +
                        ".jpashop.repository.order.query.OrderItemQueryDto(oi.order.id, i.name," +
                        " oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        return orderItems.stream()
                .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));

    }

    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery(
                        "select new jpabook.jpashop.repository.order.query.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                                " from Order o" +
                                " join o.member m" +
                                " join o.delivery d" +
                                " join o.orderItems oi" +
                                " join oi.item i", OrderFlatDto.class)
                .getResultList();
    }
}
