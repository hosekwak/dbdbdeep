package com.example.database_project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "list_file_table")
public class ListFileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFilename;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id")
    private ListEntity listEntity;

    public static ListFileEntity toListFileEntity(ListEntity listEntity, String originalFileName, String storedFilename ) {
        ListFileEntity listFileEntity = new ListFileEntity();
        listFileEntity.setOriginalFileName(originalFileName);
        listFileEntity.setStoredFilename(storedFilename);
        listFileEntity.setListEntity(listEntity);
        return listFileEntity;
    }

}
