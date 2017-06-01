package com.hexandria.auth.common.score;

import java.util.List;

/**
 * Created by frozenfoot on 01.06.17.
 */
public interface ScoreManager {
    public List<ScoreEntity> getTopScore(int size);
}
