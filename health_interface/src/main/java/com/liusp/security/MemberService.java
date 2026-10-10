package com.liusp.security;

import com.liusp.pojo.Member;

public interface MemberService {
    public void add(Member member);
    public Member findByTelephone(String telephone);
}
