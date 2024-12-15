package com.example.database_project.service;

import com.example.database_project.dto.ListDTO;
import com.example.database_project.entity.ListEntity;
import com.example.database_project.entity.ListFileEntity;
import com.example.database_project.entity.MemberEntity;
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



    // 저장
    @Transactional
    public void save(ListDTO listDTO, HttpSession session) throws IOException {
        Long memberId = (Long) session.getAttribute("id");
        if (memberId == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        // MemberEntity 조회
        MemberEntity member = memberRepository.findBymId(memberId);


        boolean fileExists = listDTO.getListFile() != null &&
                listDTO.getListFile().stream().anyMatch(file -> file != null && !file.isEmpty());

        ListEntity listEntity;

        if (!fileExists) { // 파일이 첨부되지 않은 경우
            listEntity = ListEntity.toSaveEntity(listDTO, member);
        } else { // 파일이 첨부된 경우
            listEntity = ListEntity.toSaveFileEntity(listDTO, member);
        }

        ListEntity savedListEntity = listRepository.save(listEntity);

        // 파일이 첨부된 경우에만 파일 저장
        if (fileExists) {
            for (MultipartFile listFile : listDTO.getListFile()) {
                if (listFile != null && !listFile.isEmpty()) {
                    String originalFilename = listFile.getOriginalFilename();
                    String storedFilename = System.currentTimeMillis() + "_" + originalFilename;
                    String savePath = "C:/springboot_img/" + storedFilename;

                    listFile.transferTo(new File(savePath));

                    ListFileEntity listFileEntity = ListFileEntity.toListFileEntity(savedListEntity, originalFilename, storedFilename);
                    listFileRepository.save(listFileEntity);
                }
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

    // 업데이트
    @Transactional
    public void update(ListDTO listDTO, HttpSession session) throws IOException {
        Long memberId = (Long) session.getAttribute("id");
        if (memberId == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        // MemberEntity 조회
        MemberEntity member = memberRepository.findBymId(memberId);


        // ListEntity 조회
        ListEntity listEntity = listRepository.findByllId(listDTO.getId());


        // 필드 업데이트
        listEntity.setMember(member);
        listEntity.setListTitle(listDTO.getListTitle());
        listEntity.setListContents(listDTO.getListContents());
        listEntity.setListType(listDTO.getListType());
        listEntity.setListMenu(listDTO.getListMenu());
        listEntity.setListAddress(listDTO.getListAddress());
        listEntity.setListLike(listDTO.getListLike());
        listEntity.setFileAttached(listDTO.getFileAttached());

        listRepository.save(listEntity); // 변경 사항 저장

        if (listDTO.getListFile() != null && !listDTO.getListFile().isEmpty()) {


            // 새로운 파일 첨부
            for (MultipartFile listFile : listDTO.getListFile()) {
                String originalFilename = listFile.getOriginalFilename();
                String storedFilename = System.currentTimeMillis() + "_" + originalFilename;
                String savePath = "C:/springboot_img/" + storedFilename;

                listFile.transferTo(new File(savePath));

                ListFileEntity listFileEntity = ListFileEntity.toListFileEntity(listEntity, originalFilename, storedFilename);
                listFileRepository.save(listFileEntity);
            }
        }
    }


    // 데이터 삭제
    @Transactional
    public void deleteById(Long id, Long memberId) {
        ListEntity listEntity = listRepository.findBylId(id)
                .orElseThrow(() -> new RuntimeException("ListEntity not found"));

        // 권한 확인
        if (!listEntity.getMember().getId().equals(memberId)) {
            throw new RuntimeException("You are not authorized to delete this item!");
        }

        listRepository.deleteList(listEntity.getLid()); // CascadeType.REMOVE에 의해 ListFileEntity도 삭제됨
    }

    public Page<ListDTO> paging(Pageable pageable) {
        int page = Math.max(pageable.getPageNumber(), 0);
        int pageLimit = 10;

        Page<ListEntity> listEntities = listRepository.findAllWithPaging(
                PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "lid"))
        );

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
    public Page<ListDTO> pagingSortByLike(Pageable pageable) {
        int page = Math.max(pageable.getPageNumber(), 0);
        int pageLimit = 10;
        Page<ListEntity> listEntities = listRepository.findAllWithPagingSortByLike(PageRequest.of(page, pageLimit));
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
    public Page<ListDTO> pagingMyFavorite(Pageable pageable, Long memberId) {
        int page = Math.max(pageable.getPageNumber(), 0);
        int pageLimit = 10;
        Page<ListEntity> listEntities = listRepository.findAllWithPagingMyFavorite(PageRequest.of(page, pageLimit), memberId);
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


    public Page<ListDTO> search(String keyword, Pageable pageable) {
        int page = Math.max(pageable.getPageNumber(), 0);
        int pageLimit = 10;
        Page<ListEntity> searchResults = listRepository.searchAllByKeyword(keyword, PageRequest.of(page, pageLimit));

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

