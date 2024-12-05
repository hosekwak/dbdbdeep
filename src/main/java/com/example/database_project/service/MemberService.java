package com.example.database_project.service;

import com.example.database_project.dto.MemberDTO;
import com.example.database_project.entity.MemberEntity;
import com.example.database_project.repository.MemberRepository;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public void saveMember(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.saveMember(
                memberEntity.getId(),
                memberEntity.getMemberName(),
                memberEntity.getMemberEmail(),
                memberEntity.getMemberPassword()
        );
    }
    public MemberDTO login(MemberDTO memberDTO) {
        Optional<MemberEntity> byMemberEmail = memberRepository.findByEmail(memberDTO.getMemberEmail());
        if(byMemberEmail.isPresent()) {
            MemberEntity memberEntity = byMemberEmail.get();
            if(memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                return MemberDTO.toMemberDTO(memberEntity);
            }
            else{
                return null;
            }
        }
        else{
            return null;
        }
    }
}
