package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class) //Spring Boot와 Junits 사용시 선언 필요
@SpringBootTest //Spring Boot 상태로 테스트를 하기 위해 설정 필요
@Transactional // 테스트 이후 롤백
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    public void signup() throws Exception{
        //given
        Member member = new Member();
        member.setName("oh");

        //when
        Long saveId = memberService.join(member);

        em.flush();
        //then
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void deplicateMemberName() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("oh1");

        Member member2 = new Member();
        member2.setName("oh1");

        //when
        memberService.join(member1);
        memberService.join(member2);

        /* try{
            memberService.join(member2); // 예외가 발생해야 한다.
        }catch (IllegalStateException e){
            return;
        }*/

        //then
        fail("예외가 발생해야 한다.");

    }

}