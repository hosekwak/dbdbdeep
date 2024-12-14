package com.example.database_project.entity;

import com.example.database_project.dto.ListDTO;
import com.example.database_project.dto.MemberDTO;
import com.example.database_project.repository.MemberRepository;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Entity
@Setter
@Getter
@Table(name="favorite_list")
public class FavoriteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", referencedColumnName = "id") // 외래 키 설정
    private MemberEntity memberId; // MemberEntity와 관계를 맺음

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listId", referencedColumnName = "lid") // 외래 키 설정
    private ListEntity listId; // MemberEntity와 관계를 맺음

}
