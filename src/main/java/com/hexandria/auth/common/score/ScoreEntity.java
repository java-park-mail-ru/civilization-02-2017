package com.hexandria.auth.common.score;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hexandria.auth.common.user.UserEntity;

import javax.persistence.*;


/**
 * Created by frozenfoot on 01.06.17.
 */
@Entity
@Table(name = "score", schema = "public", catalog = "hexandria")
public class ScoreEntity {
    @JsonIgnore
    @OneToOne
    private UserEntity userEntity;

    @Column(name = "score")
    private int score;

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
