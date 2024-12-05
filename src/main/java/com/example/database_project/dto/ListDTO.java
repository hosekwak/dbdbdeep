package com.example.database_project.dto;


import com.example.database_project.entity.ListEntity;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ListDTO {
    private long id;
    private String userID;
    private String listTitle;
    private String listType;
    private String listMenu;
    private String listAddress;
    private int listLike;

    private LocalDateTime listCreatedTime;
    private LocalDateTime listUpdatedTime;


    public ListDTO(long id,String userID, String listTitle, String listType, String listMenu,String listAddress, int listLike) {
        this.id = id;
        this.userID = userID;
        this.listTitle = listTitle;
        this.listType = listType;
        this.listMenu = listMenu;
        this.listAddress = listAddress;
        this.listLike = listLike;
    }


    public static ListDTO toListDTO(ListEntity listEntity) {
        ListDTO listDTO = new ListDTO();
        listDTO.setId(listEntity.getLid());
        listDTO.setUserID(listEntity.getUserId());
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
