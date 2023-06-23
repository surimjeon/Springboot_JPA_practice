package jpabook.jpashop.repository.order.query;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class OrderItemQueryDto {
    //엔티티가 아닌, 화면에 뿌리려고 만든 dto니까 jsonignore사용 해서 orderid는 화면에 보여주지 않음
        @JsonIgnore
        private Long orderId;
        private String itemName;
        private int orderPrice;
        private int count;
        public OrderItemQueryDto(Long orderId, String itemName, int orderPrice,
                             int count) {
            this.orderId= orderId;
            this.itemName= itemName;
            this.orderPrice= orderPrice;
            this.count= count;
        }
}
