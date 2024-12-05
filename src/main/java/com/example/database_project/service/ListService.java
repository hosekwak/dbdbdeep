package com.example.database_project.service;

import com.example.database_project.dto.ListDTO;
import com.example.database_project.entity.ListEntity;
import com.example.database_project.repository.ListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ListService {

    private final ListRepository listRepository;


    public List<ListDTO> finALL() {
        List<ListEntity> listEntityList = listRepository.findlistAll();
        List<ListDTO> listDTOList = new ArrayList<>();
        for(ListEntity listEntity : listEntityList) {
            listDTOList.add(ListDTO.toListDTO(listEntity));
        }
        return listDTOList;
    }

    // 저장
    public void save(ListDTO listDTO) throws IOException {
        if (listDTO.getUserID() == null || listDTO.getUserID().isEmpty()) {
            listDTO.setUserID("hose");
        }
        ListEntity listEntity = ListEntity.toSaveEntity(listDTO);
        listRepository.saveList(
                listEntity.getUserId(),
                listEntity.getListTitle(),
                listEntity.getListType(),
                listEntity.getListMenu(),
                listEntity.getListLike()

        );
    }


    public ListDTO findBylID(Long id) {
        Optional<ListEntity> optionalListEntity = listRepository.findBylId(id);
        if(optionalListEntity.isPresent()) {
            ListEntity listEntity = optionalListEntity.get();
            ListDTO listDTO = ListDTO.toListDTO(listEntity);
            return listDTO;
        }
        else {
            return null;
        }
    }

    public ListDTO update(ListDTO listDTO) {

        if (listDTO.getUserID() == null || listDTO.getUserID().isEmpty()) {
            listDTO.setUserID("hose");
        }
        ListEntity listEntity = ListEntity.toUpdateEntity(listDTO);
        listRepository.updateList(
                listEntity.getUserId(),
                listEntity.getListTitle(),
                listEntity.getListType(),
                listEntity.getListMenu(),
                listEntity.getListLike(),
                listEntity.getLid()
        );
        return findBylID(listDTO.getId());
    }

    public void deleteById(Long id) {
        listRepository.deleteBylId(id);
    }


}

