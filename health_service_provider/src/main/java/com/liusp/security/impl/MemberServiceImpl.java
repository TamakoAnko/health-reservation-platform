package com.liusp.security.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.liusp.dao.MemberDao;
import com.liusp.pojo.Member;
import com.liusp.security.MemberService;
import com.liusp.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;

    //根据手机号查询会员
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }


    //新增会员
    public void add(Member member) {
        if(member.getPassword() != null){
        member.setPassword(MD5Utils.md5(member.getPassword()));
    }
        memberDao.add(member);
    }
}
