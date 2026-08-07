package com.yourgirlseta.hollowKnight.model.charms;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.yourgirlseta.hollowKnight.model.enums.CharmType;

public class CharmPickup {
    public final Rectangle bounds;
    public final CharmType charmType;
    public boolean collected = false;
    public final Texture texture;

    public CharmPickup(Rectangle bounds, CharmType charmType, Texture texture) {
        this.bounds = bounds;
        this.charmType = charmType;
        this.texture = texture;
    }

    public void dispose() {
        texture.dispose();
    }
}

