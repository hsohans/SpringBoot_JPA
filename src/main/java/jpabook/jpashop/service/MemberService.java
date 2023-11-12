package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
//@AllArgsConstructor //아래 생성자를 대신해서 만들어주는 역할, Rombok
@RequiredArgsConstructor // final로 선언된 변수만 인젝션 자동 선언 사용 , Rombok
public class MemberService {

    /**
     * 아래 변수는 변경될 일이 없으므로 final로 선언
     * final 선언 시 컴파일에 필요한 상황을 체크함
     */
    private final MemberRepository memberRepository;

    /**
     * 애플리케이션 실행될때, 해당 값을 변경할 일이 없을 것(memberRepository). 아래 방식으로 권장
     * @param memberRepository
     */
    /*@Autowired
    public void setMemberRepository (MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }*/

    /**
     * 생성자 인젝션 방식으로 권장되어 사용됨 / 테스트 케이스도 작성할 때 용이함
     * Spring에서 자동으로 세팅되게끔 설정해주므로, @Autowired 명시 필요 없음
     * @param memberRepository
     */
    /*public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository
    }*/

    /**
     * 회원가입
     * @param member
     * @return
     */
    @Transactional
    public Long join(Member member){
        vaildateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    public void vaildateDuplicateMember(Member member){
        //Exception
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
           throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    //회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findMember(Long memberId){
        return memberRepository.findOne(memberId);
    }
}
