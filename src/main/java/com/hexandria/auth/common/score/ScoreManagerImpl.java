package com.hexandria.auth.common.score;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by frozenfoot on 01.06.17.
 */
public class ScoreManagerImpl implements ScoreManager {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<ScoreEntity> getTopScore(int size){
        List<ScoreEntity> topUsers = 
    }
}
