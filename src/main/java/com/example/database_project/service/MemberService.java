package com.example.database_project.service;

import com.example.database_project.dto.MemberDTO;
import com.example.database_project.entity.MemberEntity;
import com.example.database_project.repository.MemberRepository;
import lombok.Setter;
import org.springframework.stereotype.Service;
@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void saveMember(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.saveMember(memberEntity.getId(),memberEntity.getMemberName(), memberEntity.getMemberEmail(), memberEntity.getMemberPassword());
    }
}
