package com.yourgirlseta.hollowKnight.model.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class SecretMask {

    public Rectangle bounds;

    private boolean visible;

    private Texture texture;

    public SecretMask(
        float x,
        float y,
        float width,
        float height
    ) {

        this.bounds =
            new Rectangle(
                x,
                y,
                width,
                height
            );

        this.visible = true;

        texture =
            new Texture(
                "Architecture & Environment/Area specfic architecture/Forgotten Crossroads/Screenshot 2026-07-03 185154.png"
            );
    }

    public void reveal() {

        visible = false;
    }

    public boolean isVisible() {

        return visible;
    }

    public void render(SpriteBatch batch) {

        if (!visible) {
            return;
        }

        batch.draw(

            texture,

            bounds.x,
            bounds.y,

            bounds.width,
            bounds.height
        );
    }
}

