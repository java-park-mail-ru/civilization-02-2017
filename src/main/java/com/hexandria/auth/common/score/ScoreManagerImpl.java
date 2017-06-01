package com.hexandria.auth.common.score;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
                manager.createQuery("SELECT s.userEntity.login, s.score FROM ScoreEntity s " +
                        " ORDER BY score DESC").setMaxResults(size).getResultList();
        return topUsers;
    }

    @Override
    @Transactional
    public void incrementScore(Long userId){
        Query query = manager.createQuery("UPDATE ScoreEntity s SET s.score = s.score + 1 WHERE s.userEntity.id = :userId");
        query.setParameter("userId", userId.intValue());
        query.executeUpdate();
    }
}
