package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
}


