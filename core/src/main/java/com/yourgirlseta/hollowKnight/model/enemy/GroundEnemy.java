package com.yourgirlseta.hollowKnight.model.enemy;

import com.yourgirlseta.hollowKnight.model.character.Player;
import com.yourgirlseta.hollowKnight.model.map.FirstMap;

public abstract class GroundEnemy
    extends Enemy {

    protected float speed;

    protected int direction = 1;

    @Override
    public void update(
        float delta,
        FirstMap map,
        Player player
    ) {

        bounds.x +=
            speed * direction * delta;
    }
}
