package com.hexandria.auth.common.score;

import com.hexandria.auth.common.user.UserEntity;

import javax.persistence.*;


/**
 * Created by frozenfoot on 01.06.17.
 */
@Entity
@Table(name = "score", schema = "public", catalog = "hexandria")
public class ScoreEntity {
    @Id
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "user_")
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
