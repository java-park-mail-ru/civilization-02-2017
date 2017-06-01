package com.hexandria.controllers;

import com.hexandria.auth.common.user.UserManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * Created by frozenfoot on 01.06.17.
 */
@RestController
@CrossOrigin
@RequestMapping(value = "scoreboard")
@Transactional
public class ScoreboardController {
    final Logger LOGGER = LoggerFactory.getLogger(ScoreboardController.class);

    public ScoreboardController(@NotNull UserManager userManager){
        this.userManager = userManager;
    }

    @NotNull
    private final UserManager userManager;

    @PostMapping
    public ResponseEntity getBestPlayers(){

    }
}
