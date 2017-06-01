package com.hexandria.controllers;

import com.hexandria.auth.common.score.ScoreManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


/**
 * Created by frozenfoot on 01.06.17.
 */
@RestController
@CrossOrigin
@RequestMapping(value = "scoreboard")
@Transactional
public class ScoreboardController {
    final Logger LOGGER = LoggerFactory.getLogger(ScoreboardController.class);

    public ScoreboardController(@NotNull ScoreManager scoreManager){
        this.scoreManager = scoreManager;
    }

    @NotNull
    private final ScoreManager scoreManager;

    @GetMapping
    public ResponseEntity getBestPlayers(){
        return ResponseEntity.ok(scoreManager.getTopScore(10));
    }
}
