package com.example.database_project.service;

import com.example.database_project.dto.ListDTO;
import com.example.database_project.dto.MemberDTO;
import com.example.database_project.entity.FavoriteEntity;
import com.example.database_project.entity.ListEntity;
import com.example.database_project.entity.MemberEntity;
import com.example.database_project.repository.FavoriteRepository;
import com.example.database_project.repository.ListRepository;
import com.example.database_project.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final ListRepository listRepository;

    public FavoriteService(FavoriteRepository favoriteRepository, ListRepository listRepository) {
        this.favoriteRepository = favoriteRepository;
        this.listRepository = listRepository;
    }

    public void addFavorite(Long listID, Long memberID) {
        System.out.println("favorite mid result: " + memberID);
        System.out.println("favorite lid result: " + listID);

        if(favoriteRepository.findByBothID(memberID, listID).isEmpty()) {
            favoriteRepository.saveFavorite(memberID, listID);
            listRepository.incrementLike(listID);
        }
        else{
            favoriteRepository.deleteFavorite(memberID, listID);
            listRepository.decrementLike(listID);
        }
    }
}
