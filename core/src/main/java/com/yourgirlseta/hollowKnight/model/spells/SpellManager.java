package com.yourgirlseta.hollowKnight.model.spells;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.yourgirlseta.hollowKnight.model.charms.CharmManager;
import com.yourgirlseta.hollowKnight.model.enemy.Enemy;
import com.yourgirlseta.hollowKnight.model.enums.CharmType;
import com.yourgirlseta.hollowKnight.model.enums.SpellType;
import com.yourgirlseta.hollowKnight.model.map.FirstMap;

public class SpellManager {

    private Array<VengefulSpirit> spirits = new Array<>();
    private Array<HowlingWraiths> wraiths = new Array<>();
    private boolean spellCastThisFrame = false;

    private CharmManager charmManager;

    public SpellManager(CharmManager charmManager) {
        this.charmManager = charmManager;
    }

    public void spawn(
        SpellType type,
        float x,
        float y,
        int direction
    ) {
        spellCastThisFrame = true;
        switch (type) {
            case VENGEFUL_SPIRIT:
                spirits.add(
                    new VengefulSpirit(
                        x,
                        y,
                        direction,
                        charmManager
                    )
                );

                break;

            case HOWLING_WRAITHS:
                wraiths.add(
                    new HowlingWraiths(
                        x,
                        y + 60f,
                        charmManager
                    )
                );

                break;
        }
    }

    public void update(
        float delta,
        FirstMap map,
        Array<Enemy> enemies
    ) {

        for (
            int i = wraiths.size - 1;
            i >= 0;
            i--
        ) {

            HowlingWraiths spell =
                wraiths.get(i);

            spell.update(
                delta,
                enemies
            );

            if (spell.isFinished()) {

                wraiths.removeIndex(i);
            }
        }

        for (
            int i = spirits.size - 1;
            i >= 0;
            i--
        ) {

            VengefulSpirit spirit =
                spirits.get(i);

            spirit.update(
                delta
            );

            boolean remove = false;

            for (
                Rectangle tile :
                map.getGroundTiles()
            ) {

                if (
                    spirit.getBounds()
                        .overlaps(tile)
                ) {

                    remove = true;
                    break;
                }
            }

            for (
                Enemy enemy :
                enemies
            ) {

                if (
                    spirit.getBounds()
                        .overlaps(
                            enemy.getBounds()
                        )
                ) {

                    if (!spirit.hasHit(enemy)) {
                        int damage = Math.round(2 * charmManager.getSpellDamageMultiplier());
                        enemy.takeDamage(damage);
                        spirit.markHit(enemy);
                    }
                }
            }

            if (remove) {

                spirits.removeIndex(i);
            }
        }
    }

    public boolean didCastSpell() {
        return spellCastThisFrame;
    }

    public void consumeSpellCast() {
        spellCastThisFrame = false;
    }

    public void render(
        SpriteBatch batch
    ) {

        for (VengefulSpirit spirit : spirits) {
            spirit.render(batch);
        }

        for (HowlingWraiths spell : wraiths) {
            spell.render(batch);
        }
    }

    public void dispose() {

        for (VengefulSpirit spirit : spirits) {
            spirit.dispose();
        }

    }
}
