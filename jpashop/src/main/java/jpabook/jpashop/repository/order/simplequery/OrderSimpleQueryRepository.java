package jpabook.jpashop.repository.order.simplequery;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private final EntityManager em;
    public List<OrderSimpleQueryDto> findOrderDtos() {
    //new명령어를 사용해서 JPQL의 결과를 DTO로 바로 변환
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, n.name, " +
                        "o.orderDate, o.status, d.address)"+
                        "from Order o" +
                        "join o.member m" +
                        "join o.delivery d", OrderSimpleQueryDto.class).getResultList();
    }
}
