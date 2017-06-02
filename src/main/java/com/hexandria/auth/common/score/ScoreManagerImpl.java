package com.hexandria.auth.common.score;

import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by frozenfoot on 01.06.17.
 */
@Service
public class ScoreManagerImpl implements ScoreManager {

    @PersistenceContext
    private EntityManager entityManager;

    private List<ScoreEntity> cachedTop10 = new ArrayList<>();
    private long latestCacheUpdateTime = 0;
    public static final long CACHE_UPDATE_PERIOD = 300 * 1000; //5 min

    @Override
    public List<ScoreEntity> getTopScore() {
        final long currentTime = System.currentTimeMillis();
        if (cachedTop10.isEmpty() || latestCacheUpdateTime + CACHE_UPDATE_PERIOD < currentTime) {
            cachedTop10.clear();
            cachedTop10.addAll(entityManager.createQuery("SELECT s.userEntity.login, s.score FROM ScoreEntity s " +
                    " ORDER BY score DESC").setMaxResults(10).getResultList());
            latestCacheUpdateTime = currentTime;
        }

        return cachedTop10;
    }

    @Override
    @Transactional
    public void incrementScore(Long userId) {

        if (getByUserID(userId) == null) {
            final Query q = entityManager.createNativeQuery("INSERT INTO score(user_, score) VALUES(?1, ?2)");
            q.setParameter(1, userId);
            q.setParameter(2, 1);
            q.executeUpdate();
        } else{
            final Query query = entityManager.createQuery("UPDATE ScoreEntity s SET s.score = s.score + 1 WHERE s.userEntity.id = :userId");
            query.setParameter("userId", userId.intValue());
            query.executeUpdate();
        }

    }

    @Nullable
    @Override
    public ScoreEntity getByUserID(Long userId) {
        final Query query = entityManager.createQuery("FROM ScoreEntity WHERE userEntity.id = :userId");
        query.setParameter("userId", userId.intValue());
        ScoreEntity result = null;
        try{
            result = (ScoreEntity)query.getSingleResult();
        } catch (NoResultException e){

        }
        return result;
    }
}
