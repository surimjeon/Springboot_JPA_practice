package jpabook.jpashop.domain.Item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("M") //구분하는 값
@Getter @Setter
public class Movie extends Item{
    //상속하는 클래스
    private String director;
    private String actor;
}
