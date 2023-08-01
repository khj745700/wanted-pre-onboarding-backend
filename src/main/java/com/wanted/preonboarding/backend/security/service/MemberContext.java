package com.wanted.preonboarding.backend.security.service;

import com.wanted.preonboarding.backend.entity.Member;
import org.springframework.security.core.userdetails.User;
import java.util.Collections;


public class MemberContext extends User {

    private final Member member;

    public MemberContext(Member member) {
        super(member.getUsername(), member.getPassword(), Collections.emptyList());
        this.member = member;

    }

    public Member getMember() {
        return member;
    }

}



