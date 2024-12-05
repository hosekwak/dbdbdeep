package com.example.database_project.dto;


import com.example.database_project.entity.ListEntity;
import com.example.database_project.entity.MemberEntity;
import lombok.*;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ListDTO {
    private long id;
    private long memberId;
    private String listTitle;
    private String listType;
    private String listMenu;
    private String listAddress;
    private int listLike;

    private LocalDateTime listCreatedTime;
    private LocalDateTime listUpdatedTime;


    public ListDTO(long id, long memberId, String listTitle, String listType, String listMenu,String listAddress, int listLike) {
        this.id = id;
        this.memberId = memberId;
        this.listTitle = listTitle;
        this.listType = listType;
        this.listMenu = listMenu;
        this.listAddress = listAddress;
        this.listLike = listLike;
    }


    public static ListDTO toListDTO(ListEntity listEntity) {
        ListDTO listDTO = new ListDTO();
        listDTO.setId(listEntity.getLid());
        listDTO.setMemberId(listEntity.getMember().getId());
        listDTO.setListTitle(listEntity.getListTitle());
        listDTO.setListType(listEntity.getListType());
        listDTO.setListMenu(listEntity.getListMenu());
        listDTO.setListAddress(listEntity.getListAddress());
        listDTO.setListLike(listEntity.getListLike());
        listDTO.setListCreatedTime(listEntity.getListCreatedTime());
        listDTO.setListUpdatedTime(listEntity.getListUpdatedTime());

        return listDTO;
    }

}
