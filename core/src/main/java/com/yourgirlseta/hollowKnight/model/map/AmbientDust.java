package com.yourgirlseta.hollowKnight.model.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class AmbientDust {

    private Texture texture;

    private Array<Vector2> particles =
        new Array<>();

    public AmbientDust() {

        texture =
            new Texture(
                "Particles & Effects/ground_plink_smaller0005.png"
            );

        for (int i = 0; i < 80; i++) {

            particles.add(
                new Vector2(
                    MathUtils.random(0, 6000),
                    MathUtils.random(0, 3000)
                )
            );
        }
    }

    public void update(float delta) {

        for (Vector2 p : particles) {

            p.y -= 10f * delta;

            p.x += MathUtils.random(
                -5f,
                5f
            ) * delta;

            if (p.y < 0) {

                p.y = 3000;

                p.x =
                    MathUtils.random(
                        0,
                        6000
                    );
            }
        }
    }

    public void render(
        SpriteBatch batch
    ) {

        for (Vector2 p : particles) {

            batch.draw(
                texture,
                p.x,
                p.y,
                12,
                12
            );
        }
    }
}
