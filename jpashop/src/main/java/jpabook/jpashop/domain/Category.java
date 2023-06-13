package jpabook.jpashop.domain;

import jpabook.jpashop.domain.Item.Item;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.procedure.spi.ParameterRegistrationImplementor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name="category_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity; //재고 양

    @ManyToMany
    @JoinTable(name="category_item",
            joinColumns = @JoinColumn(name="category_id"), //중개테이블의 category_id
            inverseJoinColumns = @JoinColumn(name = "item_id")) //중개테이블의 item으로 들어가는 id
    //다대다는 매핑해줄 수 있는 테이블이 필요- 필드를 추가하지 못해서 실무에서 사용x
    private List<Item> items = new ArrayList<>();

    //일반적으로 카테고리는 계층 구조로 이뤄짐(self join)
    @ManyToOne(fetch = FetchType.LAZY) //부모는 1개, 나는 여러명(many는 나를 기준으로 해야함)
    @JoinColumn(name="parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>(); //부모들을 리스트로 받음

    //자식을 부모 카테고리에 추가
    public void addChildCategory(Category child) {
        this.child.add(child); //child리스트에 매개변수로 받은 child를 추가
        child.setParent(this); //자식을 현재 parent에 할당
    }

}
