package org.example.shallweeatbackend.service;

import java.util.ArrayList;
import java.util.List;

import org.example.shallweeatbackend.dto.SearchWordResponse;
import org.example.shallweeatbackend.entity.SearchWord;
import org.example.shallweeatbackend.entity.User;
import org.example.shallweeatbackend.exception.UserNotFoundException;
import org.example.shallweeatbackend.repository.SearchWordRepository;
import org.example.shallweeatbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final SearchWordRepository searchWordRepository;

    private final UserRepository userRepository;

    // 검색어 저장 로직
    public void saveWord(String providerId, String word){

        User user = userRepository.findByProviderId(providerId);
        if (user == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }


        SearchWord searchWord = new SearchWord();
        searchWord.setUser(user);
        searchWord.setWord(word);

        searchWordRepository.save(searchWord);
    }

    // 검색어 조회 로직
    public List<SearchWordResponse> getSaveWordsList(String providerId){

        User user = userRepository.findByProviderId(providerId);
        if (user == null) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }

        List<SearchWord> searchWords = searchWordRepository.findByUser(user);

        List<SearchWordResponse> responseList = new ArrayList<>();
        for (SearchWord searchWord : searchWords) {
            SearchWordResponse response = new SearchWordResponse();
            response.setWord(searchWord.getWord());
            response.setCreatedAt(searchWord.getCreatedAt());
            responseList.add(response);
        }

        return responseList;
    }

}
