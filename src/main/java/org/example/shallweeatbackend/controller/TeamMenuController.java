package org.example.shallweeatbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.shallweeatbackend.service.TeamMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teamboards")
@RequiredArgsConstructor
public class TeamMenuController {

    private TeamMenuService teamMenuService;

    @Autowired
    public TeamMenuController(TeamMenuService teamMenuService){
        this.teamMenuService = teamMenuService;
    }

    // 팀 메뉴 생성


}
