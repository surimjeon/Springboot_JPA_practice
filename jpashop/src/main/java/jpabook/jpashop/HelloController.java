package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("hello") //hello라는 이름의 html과 mapping해줌
    public String hello(Model model) { //Model을 파라미터로 받아와서 data속성에 hello 값 넣기
        model.addAttribute("data", "hello!!");
        return "hello"; //hello.html로 return을 해준다
    }
}