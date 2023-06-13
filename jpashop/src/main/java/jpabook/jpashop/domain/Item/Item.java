package jpabook.jpashop.domain.Item;

import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
//상속의 전략
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//가장 정교화된 스타일, single테이블은 한테이블에 다 때려넣는것,(dtype으로 구분)
@DiscriminatorColumn(name="dtype") //구분 컬럼
public class Item {
    @Id @GeneratedValue
    @Column(name="item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    //manytomany, 리스트이름이 items이기 떄문에 map시 지정
    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

}
