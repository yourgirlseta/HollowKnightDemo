package com.yourgirlseta.hollowKnight.model.map;

import com.badlogic.gdx.graphics.Texture;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class AmbientLeaves {

    private Texture texture;

    private Array<LeafParticle> particles =
        new Array<>();

    private static final float
        WORLD_WIDTH = 6000f;

    private static final float
        WORLD_HEIGHT = 3000f;

    public AmbientLeaves() {

        texture =
            new Texture(
                "Particles & Effects/falling_leaf_particles.png"
            );

        for (int i = 0; i < 60; i++) {

            particles.add(
                new LeafParticle(
                    MathUtils.random(0f, WORLD_WIDTH),
                    MathUtils.random(0f, WORLD_HEIGHT)
                )
            );
        }
    }

    public void update(float delta) {

        for (LeafParticle leaf : particles) {

            leaf.position.y -=
                leaf.speedY * delta;

            leaf.position.x +=
                MathUtils.sinDeg(
                    leaf.waveTime
                ) * leaf.waveStrength * delta;

            leaf.waveTime +=
                90f * delta;

            leaf.rotation +=
                leaf.rotationSpeed * delta;

            if (leaf.position.y < -32f) {

                leaf.position.y =
                    WORLD_HEIGHT;

                leaf.position.x =
                    MathUtils.random(
                        0f,
                        WORLD_WIDTH
                    );
            }
        }
    }

    public void render(
        SpriteBatch batch
    ) {

        for (LeafParticle leaf : particles) {

            batch.draw(
                texture,

                leaf.position.x,
                leaf.position.y,

                8f,
                8f,

                16f,
                16f,

                1f,
                1f,

                leaf.rotation,

                0,
                0,

                texture.getWidth(),
                texture.getHeight(),

                false,
                false
            );
        }
    }

    public void dispose() {

        texture.dispose();
    }

    private static class LeafParticle {

        Vector2 position;

        float speedY;

        float waveStrength;

        float waveTime;

        float rotation;

        float rotationSpeed;

        public LeafParticle(
            float x,
            float y
        ) {

            position =
                new Vector2(x, y);

            speedY =
                MathUtils.random(
                    20f,
                    60f
                );

            waveStrength =
                MathUtils.random(
                    15f,
                    40f
                );

            waveTime =
                MathUtils.random(
                    0f,
                    360f
                );

            rotation =
                MathUtils.random(
                    0f,
                    360f
                );

            rotationSpeed =
                MathUtils.random(
                    -50f,
                    50f
                );
        }
    }
}
