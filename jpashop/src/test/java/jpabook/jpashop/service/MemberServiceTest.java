package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) //junit실행할 때 스프링과 테스트 통합
@SpringBootTest //스프링테스트 선언
@Transactional //테스트 반복가능하도록 rollback
public class MemberServiceTest {
    @Autowired MemberService memberService; //의존관계주입
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception {
        //given(세팅)
        Member member = new Member();
        member.setName("kim");
        //when(메소드 실행하면) ->ID반환
        Long saveId = memberService.join(member);
        //then(검증) 새로만든 맴버와 DB에서 가져온 맴버가 같은지
        assertEquals(member, memberRepository.findOne(saveId));

    }

//    중복회원 테스트
    @Test(expected = IllegalStateException.class) //try catch대신
    public void 중복회원() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when(서비스에서 만든 메소드를 실행하면)
        memberService.join(member1);
        memberService.join(member2); //exception이 터져서 아래 then으로 가면 안됨

        //then
        fail("예외가 발생했다"); //여기까지 오면 fail로 경고
    }
}