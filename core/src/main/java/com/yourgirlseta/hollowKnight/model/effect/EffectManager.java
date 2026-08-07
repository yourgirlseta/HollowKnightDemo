package com.yourgirlseta.hollowKnight.model.effect;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.yourgirlseta.hollowKnight.model.enums.AttackDirection;
import com.yourgirlseta.hollowKnight.model.map.RubbleParticle;

public class EffectManager {

    private Array<HitEffect> effects = new Array<>();
    private Array<RubbleParticle> rubbleParticles = new Array<>();

    private Animation<TextureRegion> slashForward;
    private Animation<TextureRegion> slashUp;
    private Animation<TextureRegion> slashDown;
    private Animation<TextureRegion> rockSpin;
    public EffectManager() {

        slashForward = loadAnimation(
            "animation/Effects/spritesheet.png",
            349,
            186,
            4,
            0.03f,
            Animation.PlayMode.NORMAL
        );

        slashUp = loadAnimation(
            "animation/Effects/UpSlashEffect.png",
            169,
            192,
            4,
            0.03f,
            Animation.PlayMode.NORMAL
        );

        slashDown = loadAnimation(
            "animation/Effects/DownSlashEffect.png",
            182,
            209,
            4,
            0.03f,
            Animation.PlayMode.NORMAL
        );

        rockSpin = loadAnimation(
            "spritesheet (1).png",
            53,
            79,
            6,
            0.05f,
            Animation.PlayMode.NORMAL
        );
    }

    public void addEffect(
        HitEffect effect
    ) {

        effects.add(effect);
    }

    public void update(float delta) {

        for (int i = effects.size - 1; i >= 0; i--) {

            HitEffect effect =
                effects.get(i);

            effect.update(delta);

            if (effect.isFinished()) {

                effects.removeIndex(i);
            }
        }

        for (int i = rubbleParticles.size - 1; i >= 0; i--) {
            RubbleParticle p = rubbleParticles.get(i);
            p.update(delta);
            if (p.isFinished()) {
                rubbleParticles.removeIndex(i);
            }
        }
    }

    public void spawnSlashEffect(
        AttackDirection direction,
        float x,
        float y
    ) {
        Animation<TextureRegion> anim;

        switch (direction) {

            case UP:
                anim = slashUp;
                break;

            case DOWN:
                anim = slashDown;
                break;

            default:
                anim = slashForward;
                break;
        }

        effects.add(
            new HitEffect(
                anim,
                x,
                y
            )
        );
    }

    public void spawnWallBreakEffect(float x, float y) {
        int particleCount = MathUtils.random(6, 10);

        for (int i = 0; i < particleCount; i++) {
            float angle = MathUtils.random(20f, 160f);
            float speed = MathUtils.random(150f, 400f);

            float velocityX = MathUtils.cosDeg(angle) * speed * (MathUtils.randomBoolean() ? 1 : -1);
            float velocityY = MathUtils.sinDeg(angle) * speed;

            float life = MathUtils.random(0.5f, 0.9f);

            rubbleParticles.add(
                new RubbleParticle(rockSpin, x, y, velocityX, velocityY, life)
            );
        }
    }

    private Animation<TextureRegion> loadAnimation(
        String path,
        int width,
        int height,
        int count,
        float duration,
        Animation.PlayMode mode
    ) {

        Texture sheet =
            new Texture(path);

        TextureRegion[][] temp =
            TextureRegion.split(
                sheet,
                width,
                height
            );

        int availableFrames = temp.length * (temp.length > 0 ? temp[0].length : 0);
        if (availableFrames < count) {
            System.err.println("WARNING: File " + path + " only has " + availableFrames +
                " frames, but you requested " + count + "!");
            count = availableFrames;
        }

        TextureRegion[] frames =
            new TextureRegion[count];

        int index = 0;

        for (int r = 0; r < temp.length; r++) {

            for (int c = 0; c < temp[r].length; c++) {

                if (index < count) {

                    frames[index++] =
                        temp[r][c];
                }
            }
        }

        Animation<TextureRegion> anim =
            new Animation<>(
                duration,
                frames
            );

        anim.setPlayMode(mode);

        return anim;
    }

    public void render(SpriteBatch batch) {
        for (HitEffect effect : effects) {

            effect.render(batch);
        }

        for (RubbleParticle p : rubbleParticles) {
            p.render(batch);
        }
    }
}
