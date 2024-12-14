package com.example.database_project.service;

import com.example.database_project.dto.ListDTO;
import com.example.database_project.entity.ListEntity;
import com.example.database_project.entity.ListFileEntity;
import com.example.database_project.repository.ListFileRepository;
import com.example.database_project.repository.ListRepository;
import com.example.database_project.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.hibernate.query.Order;
import org.springframework.core.env.PropertyResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ListService {

    private final ListRepository listRepository;
    private final ListFileRepository listFileRepository;
    private final MemberRepository memberRepository;
    private final PropertyResolver propertyResolver;

    public List<ListDTO> finALL() {
        List<ListEntity> listEntityList = listRepository.findlistAll();
        List<ListDTO> listDTOList = new ArrayList<>();
        for(ListEntity listEntity : listEntityList) {
            listDTOList.add(ListDTO.toListDTO(listEntity));
        }
        return listDTOList;
    }

    // 저장
    public void save(ListDTO listDTO, HttpSession session) throws IOException {
        if(listDTO.getListFile() == null || listDTO.getListFile().isEmpty()) {
            ListEntity listEntity = ListEntity.toSaveEntity(listDTO, session);
            listRepository.saveList(
                    (Long) session.getAttribute("id"),
                    listEntity.getListTitle(),
                    listEntity.getListContents(),
                    listEntity.getListType(),
                    listEntity.getListMenu(),
                    listEntity.getListAddress(),
                    listEntity.getListLike(),
                    listEntity.getFileAttached()

            );
        }
        else {
            ListEntity listEntity = ListEntity.toSaveFileEntity(listDTO, session);
            long savedId = listRepository.save(listEntity).getLid();
            ListEntity list = listRepository.findBylId(savedId).get();

            for(MultipartFile listFile : listDTO.getListFile()) {
                String originalFilename = listFile.getOriginalFilename();
                String stroedFilename = System.currentTimeMillis() + "_" + originalFilename;
                String savePath = "C:/springboot_img/" + stroedFilename;

                listFile.transferTo(new File(savePath));

                ListFileEntity listFileEntity = ListFileEntity.toListFileEntity(list,originalFilename,stroedFilename);
                listFileRepository.save(listFileEntity);
            }

        }
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

    public ListDTO update(ListDTO listDTO, HttpSession session) {
        ListEntity listEntity = ListEntity.toUpdateEntity(listDTO, session);
        listRepository.updateList(
                (Long) session.getAttribute("id"),
                listEntity.getListTitle(),
                listEntity.getListContents(),
                listEntity.getListType(),
                listEntity.getListMenu(),
                listEntity.getListAddress(),
                listEntity.getListLike(),
                listEntity.getLid()
        );
        return findBylID(listDTO.getId());
    }

    public void deleteById(Long id) {
        listRepository.deleteBylId(id);
    }

    public Page<ListDTO> paging(Pageable pageable) {
        Page<ListEntity> listEntities = listRepository.findAllWithPaging(pageable);

        return listEntities.map(entity -> new ListDTO(
                entity.getLid(),
                entity.getMember().getId(),
                entity.getListTitle(),
                entity.getListContents(),
                entity.getListType(),
                entity.getListMenu(),
                entity.getListAddress(),
                entity.getListLike()
        ));
    }


    @Transactional
    public void increaseLike(Long id) {
        listRepository.incrementLike(id); // 좋아요 수 증가 쿼리 실행
    }

    @Transactional
    public void decreaseLike(Long id) {
        listRepository.decrementLike(id); // 좋아요 수 증가 쿼리 실행
    }

    public Page<ListDTO> search(String keyword, Pageable pageable) {
        Page<ListEntity> searchResults = listRepository.searchAllByKeyword(keyword, pageable);

        return searchResults.map(entity -> new ListDTO(
                entity.getLid(),
                entity.getMember().getId(),
                entity.getListTitle(),
                entity.getListContents(),
                entity.getListType(),
                entity.getListMenu(),
                entity.getListAddress(),
                entity.getListLike()
        ));
    }




}

