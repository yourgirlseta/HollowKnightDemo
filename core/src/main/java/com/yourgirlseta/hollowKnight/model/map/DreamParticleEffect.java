package com.yourgirlseta.hollowKnight.model.map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

/**
 * Ambient floating "dreamcatcher" particle background.
 * Independent of theme background, so it survives theme toggles.
 * Add to any non-gameplay menu screen (MainMenu, Settings, Guide, Achievements, ...).
 */
public class DreamParticleEffect extends Actor {

    private static final String PARTICLE_PATH =
        "Particles & Effects/Default-Particle.png";

    private final Texture particleTexture;
    private final Particle[] particles;

    private final float worldWidth;
    private final float worldHeight;

    public DreamParticleEffect(float worldWidth, float worldHeight, int particleCount) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        particleTexture = new Texture(PARTICLE_PATH);
        particleTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        setTouchable(Touchable.disabled);
        setSize(worldWidth, worldHeight);

        particles = new Particle[particleCount];
        for (int i = 0; i < particleCount; i++) {
            particles[i] = new Particle();
            resetParticle(particles[i], true);
        }
    }

    private void resetParticle(Particle p, boolean randomY) {
        p.x = MathUtils.random(0f, worldWidth);
        p.y = randomY ? MathUtils.random(0f, worldHeight) : -40f;
        p.speedY = MathUtils.random(6f, 18f);
        p.driftAmplitude = MathUtils.random(15f, 40f);
        p.driftSpeed = MathUtils.random(0.5f, 1.5f);
        p.scale = MathUtils.random(0.35f, 0.85f);
        p.alpha = MathUtils.random(0.2f, 0.55f);
        p.rotation = MathUtils.random(0f, 360f);
        p.rotationSpeed = MathUtils.random(-12f, 12f);
        p.phase = MathUtils.random(0f, MathUtils.PI2);
        p.baseX = p.x;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        for (Particle p : particles) {
            p.phase += delta * p.driftSpeed;
            p.y += p.speedY * delta;
            p.x = p.baseX + MathUtils.sin(p.phase) * p.driftAmplitude;
            p.rotation += p.rotationSpeed * delta;

            if (p.y - 40f > worldHeight) {
                resetParticle(p, false);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float w = particleTexture.getWidth();
        float h = particleTexture.getHeight();

        for (Particle p : particles) {
            batch.setColor(1f, 1f, 1f, p.alpha * parentAlpha);
            batch.draw(
                particleTexture,
                p.x - (w * p.scale) / 2f,
                p.y - (h * p.scale) / 2f,
                (w * p.scale) / 2f,
                (h * p.scale) / 2f,
                w * p.scale,
                h * p.scale,
                1f, 1f,
                p.rotation,
                0, 0,
                (int) w, (int) h,
                false, false
            );
        }

        batch.setColor(1f, 1f, 1f, parentAlpha);
    }

    public void dispose() {
        particleTexture.dispose();
    }

    private static class Particle {
        float x, y, baseX;
        float speedY;
        float driftAmplitude;
        float driftSpeed;
        float scale;
        float alpha;
        float rotation;
        float rotationSpeed;
        float phase;
    }
}
