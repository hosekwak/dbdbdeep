package com.example.database_project.entity;

import com.example.database_project.dto.ListDTO;
import com.example.database_project.dto.MemberDTO;
import com.example.database_project.repository.MemberRepository;
import jakarta.persistence.*;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "list_table")
public class ListEntity extends ListBaseEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long lid;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "memberId", referencedColumnName = "id") // 외래 키 설정
   private MemberEntity member; // MemberEntity와 관계를 맺음

   @Column(name = "list_title") //음식점 이름
   private String listTitle;

   @Column(name = "list_type")//음식점 종류(한식,양식,일식,주점)
   private String listType;

   @Column(name = "list_menu")//대표 메뉴
   private String listMenu;

   @Column(name = "list_address") // 음식점 위치
   private String listAddress;

   @Column(name = "list_like")
   private int listLike = 0;

   @OneToMany(mappedBy = "listId", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
   private List<FavoriteEntity> favoriteEntities;

    public static ListEntity toSaveEntity(ListDTO listDTO, HttpSession session) {
       ListEntity listEntity = new ListEntity();
       listEntity.setMember(MemberEntity.toMemberEntity((MemberDTO) session.getAttribute("memberDTO")));
       System.out.println("session result: " + session.getAttribute("id"));
       listEntity.setListTitle(listDTO.getListTitle());
       listEntity.setListType(listDTO.getListType());
       listEntity.setListMenu(listDTO.getListMenu());
       listEntity.setListAddress(listDTO.getListAddress());
       listEntity.setListLike(listDTO.getListLike());
       return listEntity;
   }

   public static ListEntity toUpdateEntity(ListDTO listDTO, HttpSession session) {
      ListEntity listEntity = new ListEntity();
      listEntity.setLid(listDTO.getId());
      listEntity.setMember(MemberEntity.toMemberEntity((MemberDTO) session.getAttribute("memberDTO")));
      System.out.println("session result: " + session.getAttribute("id"));
      listEntity.setListTitle(listDTO.getListTitle());
      listEntity.setListType(listDTO.getListType());
      listEntity.setListMenu(listDTO.getListMenu());
      listEntity.setListAddress(listDTO.getListAddress());
      listEntity.setListLike(listDTO.getListLike());
      return listEntity;
   }


}
