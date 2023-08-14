package com.min.shop.service;

import com.min.shop.domain.Member;
import com.min.shop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    public void join() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test
    public void duplicate_exception() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("min");

        Member member2 = new Member();
        member2.setName("min");

        //when
//        memberService.join(member1);
//        memberService.join(member2); //예외 발생

        //then
        assertThrows(IllegalStateException.class,
                () -> {memberService.join(member1);
                memberService.join(member2);
        });
    }

}