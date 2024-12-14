package com.example.database_project.entity;

import com.example.database_project.dto.ListDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
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
   @JoinColumn(name = "member_id", referencedColumnName = "id") // 외래 키 설정 (데이터베이스 컬럼명 확인)
   private MemberEntity member; // MemberEntity와 관계를 맺음

   @Column(name = "list_title") // 음식점 이름
   private String listTitle;

   @Column(name = "list_contents", length = 1000)
   private String listContents;

   @Column(name = "list_type") // 음식점 종류 (한식, 양식, 일식, 주점)
   private String listType;

   @Column(name = "list_menu") // 대표 메뉴
   private String listMenu;

   @Column(name = "list_address") // 음식점 위치
   private String listAddress;

   @Column(name = "list_like")
   private int listLike = 0;

   @Column(name = "file_attached")
   private int fileAttached = 0;

   @OneToMany(mappedBy = "listEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
   private List<ListFileEntity> listFileEntityList = new ArrayList<>();

   public static ListEntity toSaveEntity(ListDTO listDTO, MemberEntity member) {
      ListEntity listEntity = new ListEntity();
      listEntity.setMember(member);
      System.out.println("session result: " + member.getId());
      listEntity.setListTitle(listDTO.getListTitle());
      listEntity.setListContents(listDTO.getListContents());
      listEntity.setListType(listDTO.getListType());
      listEntity.setListMenu(listDTO.getListMenu());
      listEntity.setListAddress(listDTO.getListAddress());
      listEntity.setListLike(listDTO.getListLike());
      listEntity.setFileAttached(0);
      return listEntity;
   }

   public static ListEntity toUpdateEntity(ListDTO listDTO, MemberEntity member) {
      ListEntity listEntity = new ListEntity();
      listEntity.setLid(listDTO.getId());
      listEntity.setMember(member);
      System.out.println("session result: " + member.getId());
      listEntity.setListTitle(listDTO.getListTitle());
      listEntity.setListContents(listDTO.getListContents());
      listEntity.setListType(listDTO.getListType());
      listEntity.setListMenu(listDTO.getListMenu());
      listEntity.setListAddress(listDTO.getListAddress());
      listEntity.setListLike(listDTO.getListLike());
      listEntity.setFileAttached(listDTO.getFileAttached());
      return listEntity;
   }

   public static ListEntity toSaveFileEntity(ListDTO listDTO, MemberEntity member) {
      ListEntity listEntity = new ListEntity();
      listEntity.setMember(member);
      System.out.println("session result: " + member.getId());
      listEntity.setListTitle(listDTO.getListTitle());
      listEntity.setListContents(listDTO.getListContents());
      listEntity.setListType(listDTO.getListType());
      listEntity.setListMenu(listDTO.getListMenu());
      listEntity.setListAddress(listDTO.getListAddress());
      listEntity.setListLike(listDTO.getListLike());
      listEntity.setFileAttached(1);
      return listEntity;
   }
}
