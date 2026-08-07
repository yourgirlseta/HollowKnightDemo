package com.yourgirlseta.hollowKnight.model.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class SoulAnimator {

    private static final int MAX_SOUL = 99;

    private final SoulManager soulManager;

    private final TextureAtlas atlas;
    private final Animation<TextureRegion> soulAnimation;
    private float stateTime;
    private final ShapeRenderer shapeRenderer;
    private float displaySoul = 0f;
    private static final float FILL_SPEED = 60f;

    public SoulAnimator(SoulManager soulManager) {

        this.soulManager = soulManager;

        atlas = new TextureAtlas(
            Gdx.files.internal(
                "animation/Soulorb.atlas"
            )
        );

        Array<TextureAtlas.AtlasRegion> regions =
            new Array<>();

        for (int i = 0; i <= 5; i++) {

            String index =
                String.format("%04d", i);

            TextureAtlas.AtlasRegion region =
                atlas.findRegion(
                    "HUD_Soulorb_fills_soul_idle"
                        + index
                );

            if (region != null) {
                regions.add(region);
            }
        }

        if (regions.size == 0) {

            throw new RuntimeException(
                "Soul animation frames not found!"
            );
        }

        soulAnimation =
            new Animation<>(
                0.08f,
                regions,
                Animation.PlayMode.LOOP
            );

        shapeRenderer = new ShapeRenderer();
        this.displaySoul = soulManager.getCurrentSoul();
    }

    public void update(float delta) {
        stateTime += delta;

        float target = soulManager.getCurrentSoul();

        if (displaySoul < target) {
            displaySoul = Math.min(target, displaySoul + FILL_SPEED * delta);
        } else if (displaySoul > target) {
            displaySoul = Math.max(target, displaySoul - FILL_SPEED * delta);
        }
    }

    public void render(SpriteBatch batch) {

        float x = 20f;
        float y = 970f;

        float size = 120f;

        float soulPercent = MathUtils.clamp(displaySoul / MAX_SOUL, 0f, 1f);

        TextureRegion frame = soulAnimation.getKeyFrame(stateTime);

        batch.end();

        Gdx.gl.glEnable(
            GL20.GL_STENCIL_TEST
        );

        Gdx.gl.glClearStencil(0);

        Gdx.gl.glClear(
            GL20.GL_STENCIL_BUFFER_BIT
        );

        Gdx.gl.glStencilFunc(
            GL20.GL_ALWAYS,
            1,
            0xFF
        );

        Gdx.gl.glStencilOp(
            GL20.GL_KEEP,
            GL20.GL_KEEP,
            GL20.GL_REPLACE
        );

        Gdx.gl.glColorMask(
            false,
            false,
            false,
            false
        );

        shapeRenderer.setProjectionMatrix(
            batch.getProjectionMatrix()
        );

        shapeRenderer.setTransformMatrix(
            batch.getTransformMatrix()
        );

        shapeRenderer.begin(
            ShapeRenderer.ShapeType.Filled
        );

        shapeRenderer.circle(
            x + size / 2f,
            y + size / 2f,
            size / 2f,
            96
        );

        shapeRenderer.end();


        Gdx.gl.glColorMask(
            true,
            true,
            true,
            true
        );

        Gdx.gl.glStencilFunc(
            GL20.GL_EQUAL,
            1,
            0xFF
        );

        Gdx.gl.glStencilOp(
            GL20.GL_KEEP,
            GL20.GL_KEEP,
            GL20.GL_KEEP
        );

        batch.begin();

        int regionWidth =
            frame.getRegionWidth();

        int regionHeight =
            frame.getRegionHeight();

        int visibleRegionHeight =
            (int) (
                regionHeight * soulPercent
            );

        if (visibleRegionHeight > 0) {

            int srcY =
                regionHeight
                    - visibleRegionHeight;

            float drawHeight =
                size * soulPercent;

            TextureRegion partialFrame =
                new TextureRegion(frame);

            partialFrame.setRegion(
                frame.getRegionX(),
                frame.getRegionY() + srcY,
                regionWidth,
                visibleRegionHeight
            );

            batch.draw(
                partialFrame,
                x,
                y,
                size,
                drawHeight
            );
        }

        batch.end();

        Gdx.gl.glDisable(
            GL20.GL_STENCIL_TEST
        );

        batch.begin();
    }

    public void dispose() {

        atlas.dispose();

        shapeRenderer.dispose();
    }
}
