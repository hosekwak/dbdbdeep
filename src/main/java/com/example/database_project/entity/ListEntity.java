package com.example.database_project.entity;

import com.example.database_project.dto.ListDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "list_table")
public class ListEntity extends ListBaseEntity {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private long lid;

   @Column(length = 20, nullable = false)
   private String userId = "hose";

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

   public static ListEntity toSaveEntity(ListDTO listDTO) {
      ListEntity listEntity = new ListEntity();
      listEntity.setUserId(listDTO.getUserID());
      listEntity.setListTitle(listDTO.getListTitle());
      listEntity.setListType(listDTO.getListType());
      listEntity.setListMenu(listDTO.getListMenu());
      listEntity.setListAddress(listDTO.getListAddress());
      listEntity.setListLike(listDTO.getListLike());
      return listEntity;
   }

   public static ListEntity toUpdateEntity(ListDTO listDTO) {
      ListEntity listEntity = new ListEntity();
      listEntity.setLid(listDTO.getId());
      listEntity.setUserId(listDTO.getUserID());
      listEntity.setListTitle(listDTO.getListTitle());
      listEntity.setListType(listDTO.getListType());
      listEntity.setListMenu(listDTO.getListMenu());
      listEntity.setListAddress(listDTO.getListAddress());
      listEntity.setListLike(listDTO.getListLike());
      return listEntity;
   }


}
