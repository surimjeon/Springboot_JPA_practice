package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {
    private Long id;
    //item필드(상속)
    private String name;
    private int price;
    private int stockQuantity;
    //book필드
    private String author;
    private String isbn;

}
