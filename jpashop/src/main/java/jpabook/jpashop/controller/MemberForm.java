package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

//입력받을 수 있는 폼 만들기
@Getter @Setter
public class MemberForm {
    @NotEmpty(message = "회원이름은 필수!")
    //이름필드는 필수로 넣을 수 있도록 유효성 검사
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
