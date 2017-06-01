package com.hexandria.auth.common.score;

import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Created by frozenfoot on 01.06.17.
 */
@Service
public class ScoreManagerImpl implements ScoreManager {

    @PersistenceContext
    private EntityManager manager;

    @Override
    public List<ScoreEntity> getTopScore(int size){
        List<ScoreEntity> topUsers = (List<ScoreEntity>)
                manager.createQuery("SELECT s FROM ScoreEntity s " + 
                        " ORDER BY score").setMaxResults(size).getResultList();
        return topUsers;
    }
}
