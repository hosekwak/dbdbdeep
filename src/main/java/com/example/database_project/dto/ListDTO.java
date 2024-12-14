package com.example.database_project.dto;


import com.example.database_project.entity.ListEntity;
import com.example.database_project.entity.ListFileEntity;
import com.example.database_project.entity.MemberEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ListDTO {
    private long id;
    private long memberId;
    private String listTitle;
    private String listContents;
    private String listType;
    private String listMenu;
    private String listAddress;
    private int listLike;
    private LocalDateTime listCreatedTime;
    private LocalDateTime listUpdatedTime;

    private List<MultipartFile> ListFile;
    private List<String> originalFileName;
    private List<String> storedFileName;
    private int fileAttached;


    public ListDTO(long id, long memberId, String listTitle,String listContents, String listType, String listMenu,String listAddress, int listLike) {
        this.id = id;
        this.memberId = memberId;
        this.listTitle = listTitle;
        this.listContents = listContents;
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
        listDTO.setListContents(listEntity.getListContents());
        listDTO.setListType(listEntity.getListType());
        listDTO.setListMenu(listEntity.getListMenu());
        listDTO.setListAddress(listEntity.getListAddress());
        listDTO.setListLike(listEntity.getListLike());
        listDTO.setListCreatedTime(listEntity.getListCreatedTime());
        listDTO.setListUpdatedTime(listEntity.getListUpdatedTime());
        if(listEntity.getFileAttached() == 0) {
            listDTO.setFileAttached(listEntity.getFileAttached());
        }
        else {
            List<String> originalFileNameList = new ArrayList<>();
            List<String> storedFileNameList = new ArrayList<>();
            listDTO.setFileAttached(listEntity.getFileAttached());
            for(ListFileEntity listFileEntity: listEntity.getListFileEntityList()) {
                originalFileNameList.add(listFileEntity.getOriginalFileName());
                storedFileNameList.add(listFileEntity.getStoredFilename());
            }
            listDTO.setOriginalFileName(originalFileNameList);
            listDTO.setStoredFileName(storedFileNameList);
        }

        return listDTO;
    }

}
