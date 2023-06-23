package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

    //엔티티 직접 노출, 양방향 문제(@JsonIgnore)
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
    List<Order> all =orderRepository.findAllByString(new OrderSearch());
    for(Order order: all) {
        order.getMember().getName(); //lazy강제 초기화
        order.getDelivery().getAddress(); //lazy강제 초기화
    }
    return all;
    }

    //v2. 엔티티를 조회해서 DTO로 변환(fetcch join사용x)
    //단점: 지연로딩으로 쿼리 N번 호출
    @GetMapping("api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream()
                .map(o->new SimpleOrderDto(o))
                .collect(toList());
        return result;
    }

    @Data
    private class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId= order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }
    //v3. 엔티티를 조회해서 DTO로 변환하기(FETCH JOIN사용)
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o->new SimpleOrderDto(o))
                .collect(toList());
        return result;
    }

    private final OrderSimpleQueryRepository orderSimpleQueryRepository;
    //위에서 만든 simple DTO클래스는 컨트롤러가 의존하고 있어서
    //레포지토리에서 의존하게 만들기 위해서 따로 클래스를 만들어줌

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> orderV4() {
        return orderSimpleQueryRepository.findOrderDtos();
    }

}


