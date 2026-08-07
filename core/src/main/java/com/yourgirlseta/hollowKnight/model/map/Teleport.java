package com.yourgirlseta.hollowKnight.model.map;

import com.badlogic.gdx.math.Rectangle;

public class Teleport {

    public Rectangle bounds;

    public String targetMap;

    public String targetSpawn;

    public Teleport(
        Rectangle bounds,
        String targetMap,
        String targetSpawn
    ) {

        this.bounds = bounds;

        this.targetMap = targetMap;

        this.targetSpawn = targetSpawn;
    }
}
