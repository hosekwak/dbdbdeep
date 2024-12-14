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
    @Transactional
    public void save(ListDTO listDTO, HttpSession session) throws IOException {
        Long memberId = (Long) session.getAttribute("id");
        if (memberId == null) {
            throw new RuntimeException("로그인이 필요합니다.");
        }

        // MemberEntity 조회
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

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
        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // ListEntity 조회
        ListEntity listEntity = listRepository.findById(listDTO.getId())
                .orElseThrow(() -> new RuntimeException("ListEntity not found"));

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
            // 기존 파일 삭제 (옵션)
            // listFileRepository.deleteAll(listEntity.getListFileEntityList());

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
        ListEntity listEntity = listRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ListEntity not found"));

        // 권한 확인
        if (!listEntity.getMember().getId().equals(memberId)) {
            throw new RuntimeException("You are not authorized to delete this item!");
        }

        listRepository.delete(listEntity); // CascadeType.REMOVE에 의해 ListFileEntity도 삭제됨
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
    public Page<ListDTO> pagingSortByLike(Pageable pageable) {
        Page<ListEntity> listEntities = listRepository.findAllWithPagingSortByLike(pageable);
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
        Page<ListEntity> listEntities = listRepository.findAllWithPagingMyFavorite(pageable, memberId);
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

