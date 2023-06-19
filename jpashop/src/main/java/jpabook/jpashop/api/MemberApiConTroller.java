package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@RestController //contoller+responseBody = RestController
//responsebody는 데이터자체를 xml, json으로 바로 보낼 때 사용
@RequiredArgsConstructor
public class MemberApiConTroller {

    //의존관계 주입
    private final MemberService memberService;

    //get
    //1. 권장하지 않는 방법
    @GetMapping("api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers(); //응답값으로 엔티티를 직접 외부에 노출
    }

    //2. 권장하는 방법 (엔티티를 DTO로 변환해서 반환)
    @GetMapping("api/v2/members")
    public Result MembersV2() {
        List<Member> findMembers = memberService.findMembers();
        //엔티티->DTO변환
        List<MemberDto> collect = findMembers.stream()
                .map(m->new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect);
    }
    @Data
    @AllArgsConstructor
    static class Result<T> { //Result클래스로 컬렉션을 감싸서 응답값을 T타입으로 만들고 나중에 필요한 필드를 추가할 수 있음
        private T data;
    }
    @Data
    @AllArgsConstructor
    static class MemberDto { //노출할 정보만 여기에 저장
        private String name;
    }

//post

    //권장하지 않는 방법
    @PostMapping("api/v1/members") //회원등록하는 api
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        //json으로 온 body를 Member에 그대로 매핑해서 반영해줌
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    //권장하는 방법(DTO로 받는 방법)
    @PostMapping("api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member(); //객체를 받아오고
        member.setName(request.getName()); //request에서 받아온 이름을 mameber에 저장
        //만약 필드 이름이 바뀌면 여기서 에러가 떠서 쉽게 에러를 파악할 수 있다
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

//put
    @PutMapping("api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id); //쿼리와 커멘드를 분리하기 위해->findMember.getId()를 위해
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor //DTO에서 모든 파라미터(id, name)를 넘기는 생성자가 필요
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }




    //요청에서 name을 데이터로 받는다(어떤 데이터를 받는지 알 수 있음
    @Data
    static class CreateMemberRequest {
        private String name;
    }


    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id; //생성자
        }
    }



}
