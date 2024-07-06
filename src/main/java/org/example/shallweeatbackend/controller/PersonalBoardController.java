package org.example.shallweeatbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.dto.CustomOAuth2User;
import org.example.shallweeatbackend.dto.PersonalBoardDTO;
import org.example.shallweeatbackend.dto.RecommendMenuDTO;
import org.example.shallweeatbackend.dto.RecommendOptionsDTO;
import org.example.shallweeatbackend.exception.PersonalBoardNotFoundException;
import org.example.shallweeatbackend.service.PersonalBoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/personalboards")
@RequiredArgsConstructor
public class PersonalBoardController {

    private final PersonalBoardService personalBoardService;

    @PostMapping
    public PersonalBoardDTO createPersonalBoard(@AuthenticationPrincipal CustomOAuth2User principal, @RequestParam String name) {
        return personalBoardService.createPersonalBoard(principal.getProviderId(), name);
    }

    @GetMapping()
    public List<PersonalBoardDTO> getPersonalBoardsByUser(@AuthenticationPrincipal CustomOAuth2User principal) {
        return personalBoardService.getPersonalBoardsByUserProviderId(principal.getProviderId());
    }

    @GetMapping("/{personalBoardId}")
    public PersonalBoardDTO getPersonalBoard(@PathVariable Long personalBoardId) {
        return personalBoardService.getPersonalBoard(personalBoardId);
    }

    @PatchMapping("/{personalBoardId}")
    public PersonalBoardDTO updatePersonalBoard(@PathVariable Long personalBoardId, @RequestParam String name) {
        return personalBoardService.updatePersonalBoard(personalBoardId, name);
    }

    @DeleteMapping("/{personalBoardId}")
    public ResponseEntity<Map<String, String>> deletePersonalBoard(@PathVariable Long personalBoardId) {
        personalBoardService.deletePersonalBoard(personalBoardId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "메뉴판이 성공적으로 삭제되었습니다.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/{personalBoardId}/recommend")
    public List<RecommendMenuDTO> recommendMenus(@PathVariable Long personalBoardId, @RequestBody RecommendOptionsDTO options) {
        return personalBoardService.recommendMenus(personalBoardId, options);
    }

    @GetMapping("/{personalBoardId}/menus")
    public List<RecommendMenuDTO> getMenusByPersonalBoardId(@PathVariable Long personalBoardId) {
        return personalBoardService.getMenusByPersonalBoardId(personalBoardId);
    }

    // 예외 처리 핸들러 추가
    @ExceptionHandler(PersonalBoardNotFoundException.class)
    public ResponseEntity<Map<String, String>> handlePersonalBoardNotFoundException(PersonalBoardNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
