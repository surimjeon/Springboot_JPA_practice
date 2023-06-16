package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    //컨트롤러는 보통 서비스를 가져다 씀(화면과 서비스를 이어주니까)
    private final MemberService memberService;

    // href '/'는 homecontroller로 들어오고 그다음 아래의 링크로 들어간다
    @GetMapping(value = "/members/new") //url매핑(home.html에 href로 지정됨)
    public String createForm(Model model) {
        //컨트롤러에서 뷰로 넘어갈 때 해당 데이터를 실어서 넘김
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm"; //해당 html로 이동
    }

    @PostMapping(value = "/members/new")
    //MemberForm클래스에 정의한 객체가 넘어오는데, @Valid : validation쓴걸(not empty) 적용한다
    public String create(@Valid MemberForm form, BindingResult result) {
        if(result.hasErrors()) {
            return "members/createMemberForm";
        }
        Address address = new Address(form.getCity(),form.getStreet(), form.getZipcode());
        Member member= new Member();
        member.setName(form.getName()); //form에서 가져와서 저장(set)
        member.setAddress(address);
        memberService.join(member); //멤버서비스의 join함수(회원가입) 사용
        return "redirect:/"; //첫번째 페이지로 넘어감
    }
    @GetMapping(value = "/members")
    public String list(Model model) { //MVC의 Model객체에 보관
        List<Member> members = memberService.findMembers();
        model.addAttribute("members",members);
        return "members/memberList"; //View(html반환)
    }
}
