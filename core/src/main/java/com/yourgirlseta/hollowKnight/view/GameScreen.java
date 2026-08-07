package com.yourgirlseta.hollowKnight.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.yourgirlseta.hollowKnight.Main;
import com.yourgirlseta.hollowKnight.model.CheatManager;
import com.yourgirlseta.hollowKnight.model.GameData;
import com.yourgirlseta.hollowKnight.model.achievement.AchievementManager;
import com.yourgirlseta.hollowKnight.model.achievement.AchievementPopupQueue;
import com.yourgirlseta.hollowKnight.model.charms.CharmManager;
import com.yourgirlseta.hollowKnight.model.charms.CharmPickup;
import com.yourgirlseta.hollowKnight.model.enemy.boss.FalseKnight;
import com.yourgirlseta.hollowKnight.model.enums.PlayerState;
import com.yourgirlseta.hollowKnight.model.settingsUtils.SettingsManager;
import com.yourgirlseta.hollowKnight.model.spells.SpellManager;
import com.yourgirlseta.hollowKnight.model.character.HealthHud;
import com.yourgirlseta.hollowKnight.model.character.Player;

import com.yourgirlseta.hollowKnight.model.character.SoulAnimator;
import com.yourgirlseta.hollowKnight.model.character.SoulManager;
import com.yourgirlseta.hollowKnight.model.effect.EffectManager;
import com.yourgirlseta.hollowKnight.model.enemy.Enemy;
import com.yourgirlseta.hollowKnight.model.map.*;
import com.yourgirlseta.hollowKnight.model.settingsUtils.ControlsManager;
import com.yourgirlseta.hollowKnight.model.zote.DialogueBox;
import com.yourgirlseta.hollowKnight.model.zote.Zote;

public class GameScreen extends ScreenAdapter {
    private float playTime = 0f;
    private final Main game;
    private final GameData gameData;

    private SpriteBatch batch;
    private FitViewport viewport;
    private OrthographicCamera camera;
    private OrthographicCamera hudCamera;
    private Texture soulBackground;
    private Texture souleyes;



    private static final float WORLD_WIDTH = 1920f;
    private static final float WORLD_HEIGHT = 1080f;

    private float shakeTime = 0f;
    private float shakeIntensity = 0f;

    private float mapWidth;
    private float mapHeight;


    private ControlsManager controlsManager;
    private Player player;
    private HealthHud healthHud;
    private SoulManager soulManager;
    private SpellManager spellManager;
    private SoulAnimator soulAnimator;
    private CharmManager charmManager;
    private FirstMap firstMap;
    private AmbientDust ambientDust;
    private AmbientLeaves ambientLeaves;
    private EffectManager effectManager;
    private CheatManager cheatManager;
    private DialogueBox dialogueBox;
    private Zote zote;
    private BitmapFont font;
    private AchievementManager achievementManager;
    private AchievementPopupQueue achievementPopupQueue;

    private boolean pausePressed =
        false;

    private boolean canOpenInventory() {
        return true;
    }


    public GameScreen(Main game, GameData gameData) {
        this.game = game;
        this.gameData = gameData;
        initializeGame();
    }

    private void initializeGame() {

        batch = new SpriteBatch();
        font = new BitmapFont();
        controlsManager = game.getControlsManager();
        camera = new OrthographicCamera();
        viewport = new FitViewport(
                WORLD_WIDTH,
                WORLD_HEIGHT,
                camera
            );

        viewport.update(
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight(),
            true
        );

        soulBackground = new Texture("animation/HUD/HUD Cln_167.png");
        souleyes = new Texture("animation/HUD/SoulOrb_Eye.png");

        camera.zoom = 0.8f;
        cheatManager = game.getCheatManager();
        charmManager = game.getCharmManager();

        healthHud = new HealthHud(cheatManager);
        soulManager = new SoulManager();
        soulManager.setSoul(gameData.spirit);
        soulAnimator = new SoulAnimator(soulManager);
        spellManager = new SpellManager(charmManager);
        hudCamera = new OrthographicCamera();

        hudCamera.setToOrtho(
            false,
            WORLD_WIDTH,
            WORLD_HEIGHT
        );

        hudCamera.update();
        firstMap = new FirstMap(
                gameData.mapName + ".tmx",
                batch,
                game
            );

        if (firstMap.getZote() != null) {
            dialogueBox = new DialogueBox(firstMap.getZote().getDialogueManager());
        }

        if (gameData.mapName.equals("Map1")) {
            game.getAudioManager().playMusic(game.getForgottonCrossRoadsMusic(), true);
        }

        if (gameData.mapName.equals("Map2")) {
            game.getAudioManager().playMusic(game.getGreenPathMusic(), true);
        }

        if (gameData.mapName.equals("MAp4")) {
            game.getAudioManager().playMusic(game.getBossRoomMusic(), true);
        }

        mapWidth = firstMap.getMapWidth();

        mapHeight = firstMap.getMapHeight();

        if (gameData.mapName.equals("Map1")) {
            ambientDust = new AmbientDust();
        } else if (
            gameData.mapName.equals("Map2")) {
            ambientLeaves = new AmbientLeaves();
        }

        Vector2 spawn = firstMap.getPlayerSpawn(gameData.spawnId);
        float spawnX = spawn.x;

        float spawnY = spawn.y;

        player = new Player(
                spawnX,
                spawnY,
                controlsManager,
                healthHud,
                soulManager,
                spellManager,
                game.getCharmManager(),
                game,
                gameData
            );

        player.setRespawnPoint(spawn);
        effectManager = new EffectManager();

        camera.position.set(Math.round(spawnX), Math.round(spawnY), 0f);
        camera.update();

        achievementManager = game.getAchievementManager();
        achievementPopupQueue = new AchievementPopupQueue(game.getSettingsManager());
        achievementManager.addListener(achievementPopupQueue);
    }

    public Player getPlayer() {

        return player;
    }


    public CheatManager getCheatManager() {
        return cheatManager;
    }

    public FirstMap getFirstMap() {
        return firstMap;
    }

    private void shakeCamera(
        float duration,
        float intensity
    ) {

        shakeTime = duration;

        shakeIntensity = intensity;
    }

    private void resolveCollision(
        Rectangle player,
        Rectangle wall
    ) {
        if (player.overlaps(wall)) {

            float overlapLeft =
                player.x + player.width - wall.x;

            float overlapRight =
                wall.x + wall.width - player.x;

            float overlapBottom =
                player.y + player.height - wall.y;

            float overlapTop =
                wall.y + wall.height - player.y;

            float minOverlapX =
                Math.min(
                    overlapLeft,
                    overlapRight
                );

            float minOverlapY =
                Math.min(
                    overlapBottom,
                    overlapTop
                );

            if (
                minOverlapX < minOverlapY
            ) {

                if (
                    overlapLeft < overlapRight
                ) {

                    player.x -= overlapLeft;

                } else {

                    player.x += overlapRight;
                }

            } else {

                if (
                    overlapBottom < overlapTop
                ) {

                    player.y -= overlapBottom;

                } else {

                    player.y += overlapTop;
                }
            }
        }
    }

    @Override
    public void show() {
        for (Enemy enemy : firstMap.getEnemies()) {
            enemy.setDeathListener(deadEnemy -> {
                gameData.enemiesKilled++;
                achievementManager.notifyEnemyDefeated(deadEnemy);
            });
        }

        viewport.update(
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight(),
            true
        );

        camera.update();
        hudCamera.update();
        restoreMusic();
    }

    private void restoreMusic() {

        switch (gameData.mapName) {

            case "Map1":

                game.getAudioManager().playMusic(
                    game.getForgottonCrossRoadsMusic(),
                    true
                );

                break;

            case "Map2":

                game.getAudioManager().playMusic(
                    game.getGreenPathMusic(),
                    true
                );

                break;

            case "Map3":

                game.getAudioManager().playMusic(
                    game.getGreenPathMusic(),
                    true
                );

                break;
        }
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hudCamera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);
        hudCamera.update();
    }

    @Override
    public void render(float delta) {
        delta = Math.min(delta, 1f / 30f);
        gameData.playTime += delta;

        if (controlsManager.isBossTeleportCheat()) {
            cheatManager.teleportToBoss();
        }

        if (controlsManager.isNoclipCheat()) {
            cheatManager.setNoclip(
                !cheatManager.isNoclip()
            );
        }

        if (controlsManager.isEmergencyHealCheat()) {
            cheatManager.setEmergencyHeal(
                !cheatManager.isEmergencyHeal()
            );
        }

        if (controlsManager.isFillSoulCheat()) {
            soulManager.setSoul(SoulManager.MAX_SOUL);
        }

        if (controlsManager.isGodModeCheat()) {
            cheatManager.setGodMode(!cheatManager.isGodMode());
        }

        if (controlsManager.isKillAllCheat()) {
            for (Enemy enemy : firstMap.getEnemies()) {
                enemy.takeDamage(9999);
            }
        }

        if (
            controlsManager.isPauseMenuPressed()
        ) {

            if (!pausePressed) {

                pausePressed = true;

                game.setScreen(
                    new PauseMenuScreen(
                        game,
                        this
                    )
                );
            }

        } else {

            pausePressed = false;
        }

        if (controlsManager.isInventoryJustPressed() && canOpenInventory()) {
            game.setScreen(new InventoryMenuScreen(game, this, charmManager));
            return;
        }

        if (ambientDust != null) {

            ambientDust.update(delta);
        }

        if (ambientLeaves != null) {
            ambientLeaves.update(delta);
        }

        player.update(delta, firstMap);
        spellManager.update(
            delta,
            firstMap,
            firstMap.getEnemies()
        );

        if (spellManager.didCastSpell()) {

            shakeCamera(0.15f, 8f);

            spellManager.consumeSpellCast();
        }

        if (firstMap.getZote() != null) {

            firstMap.getZote().update(
                delta,
                player
            );
        }

        Zote zote =
            firstMap.getZote();

        if (zote != null) {

            if (
                zote.canInteract(player)
                    &&
                    Gdx.input.isKeyJustPressed(
                        Input.Keys.E
                    )
            ) {

                zote.getDialogueManager()
                    .startDialogue();

                dialogueBox.startCurrentLine();

                game.getAudioManager()
                    .playRandomZoteVoice();
            }

            if (
                zote.getDialogueManager()
                    .isActive()
            ) {

                player.setCanMove(false);

                dialogueBox.update(delta);

                if (
                    Gdx.input.isKeyJustPressed(
                        Input.Keys.ENTER
                    )
                ) {

                    if (
                        !dialogueBox.isFinished()
                    ) {

                        dialogueBox
                            .getWriter()
                            .finishInstantly();
                    }

                    else {

                        zote.getDialogueManager()
                            .nextLine();

                        if (
                            zote.getDialogueManager()
                                .isActive()
                        ) {

                            dialogueBox.startCurrentLine();

                            game.getAudioManager()
                                .playRandomZoteVoice();
                        }

                        else {

                            dialogueBox.clear();
                        }
                    }
                }

            } else {

                player.setCanMove(true);
            }
        }

        effectManager.update(delta);
        achievementPopupQueue.update(delta);

        Rectangle attackBox =
            player.getAttackHitbox();

        if (!healthHud.isNoclip()) {
            for (Rectangle hazard :
                firstMap.getHazards()) {

                if (
                    player.bounds.overlaps(hazard)
                ) {

                    if (
                        player.isPogoAttacking()
                            &&
                            attackBox.overlaps(hazard)
                    ) {

                        player.pogoBounce();
                    }

                    else {

                        float spikeCenterX = hazard.x + hazard.width / 2f;

                        if (
                            player.takeHazardHit()
                        ) {
                            game.getAudioManager().playPlayerHit();
                            shakeCamera(0.15f, 8f);
                        }
                    }
                }
            }
        }

        boolean attackStarted =
            player.didAttackStart();

        if (attackStarted) {

            Rectangle hitbox =
                player.getAttackHitbox();

            effectManager.spawnSlashEffect(

                player.getAttackDirection(),

                hitbox.x,
                hitbox.y
            );
        }

        for (Enemy enemy :
            firstMap.getEnemies()) {

            enemy.update(delta, firstMap, player);

            if (enemy instanceof FalseKnight fk) {

                if (fk.consumeShakeRequest()) {
                    shakeCamera(0.15f, 8f);
                }

                if (fk.isHeavyMoving()) {
                    shakeCamera(0.1f, 4f);
                }

                if (fk.isDeathAnimationFinished()) {
                    game.setScreen(new VictoryScreen(game, gameData));
                    return;
                }
            }

            for (BreakableWall wall :
                firstMap.getBreakableWalls()) {

                if (!wall.isSolid()) {
                    continue;
                }

                if (
                    enemy.getBounds().overlaps(
                        wall.bounds
                    )
                ) {

                    resolveCollision(
                        enemy.getBounds(),
                        wall.bounds
                    );

                    enemy.stopMovement();
                }
            }

            if (enemy.isDead())
                continue;

            if (
                attackStarted
                    &&
                    player.isAttacking()
                    &&
                    !player.hasAttackHit()
                    &&
                    attackBox.overlaps(
                        enemy.getBounds()
                    )
            ) {
                player.markAttackHit();

                effectManager.spawnSlashEffect(
                    player.getAttackDirection(),

                    enemy.getBounds().x,
                    enemy.getBounds().y
                );

                if (player.isPogoAttacking()) {

                    player.pogoBounce();
                }

                int nailDamage = Math.round(1 * charmManager.getNailDamageMultiplier());
                enemy.takeDamage(nailDamage);

                soulManager.addSoul(Math.round(11 * charmManager.getSoulMultiplier()));
                game.getAudioManager().playSoul();

                float force = (player.isFacingRight() ? 350f : -350f) * charmManager.getKnockbackMultiplier();
                enemy.applyKnockback(force);

            }

            if (!healthHud.isNoclip()
                &&
                player.bounds.overlaps(
                    enemy.getBounds()
                )
            ) {
                    boolean sharpShadowDashing = charmManager.hasSharpShadow() && player.isDashing();


                    if (sharpShadowDashing) {

                        if (!enemy.isDead()) {
                            enemy.takeDamage(1);

                        }

                    } else {

                        float enemyCenterX = enemy.getBounds().x + enemy.getBounds().width / 2f;

                        if (player.takeHit(enemyCenterX)) {
                            game.getAudioManager().playPlayerHit();
                            shakeCamera(0.15f, 8f);

                            if (enemy instanceof FalseKnight) {
                                achievementManager.notifyPlayerTookDamageDuringFalseKnightFight();
                            }
                        }
                    }

            }
        }

        if (
            zote != null
                &&
                attackStarted
                &&
                attackBox.overlaps(
                    zote.getBounds()
                )
        ) {

            zote.hit();
        }

        if (!healthHud.isNoclip()) {
            for (
                BreakableWall wall :
                firstMap.getBreakableWalls()
            ) {

                if (
                    !wall.isSolid()
                ) {
                    continue;
                }

                if (
                    player.bounds.overlaps(
                        wall.bounds
                    )
                ) {

                    resolveCollision(
                        player.bounds,
                        wall.bounds
                    );
                    player.setVelocityX(0);
                }
            }
        }


        if (attackStarted) {

            for (
                BreakableWall wall :
                firstMap.getBreakableWalls()
            ) {

                if (
                    wall.isBroken()
                ) {
                    continue;
                }

                if (
                    attackBox.overlaps(
                        wall.bounds
                    )
                ) {

                    wall.hit();

                    effectManager.spawnSlashEffect(

                        player.getAttackDirection(),

                        wall.bounds.x,
                        wall.bounds.y
                    );



                }
            }

            player.consumeAttackStart();
        }

        for (
            BreakableWall wall :
            firstMap.getBreakableWalls()
        ) {

            wall.update(delta);
            if (wall.consumeJustBroken()) {

                firstMap.revealMasksForBrokenWalls();

                game.getAudioManager().playWallBreak();

                float centerX = wall.bounds.x + wall.bounds.width / 2f;
                float centerY = wall.bounds.y + wall.bounds.height / 2f;
                effectManager.spawnWallBreakEffect(centerX, centerY);

                shakeCamera(0.5f, 8f);
            }
        }

        for (CharmPickup pickup : firstMap.getCharmPickups()) {

            if (pickup.collected) continue;

            if (player.bounds.overlaps(pickup.bounds)) {

                pickup.collected = true;
                charmManager.unlock(pickup.charmType);
            }
        }


        for (Teleport teleport :
            firstMap.getTeleports()) {

            if (
                player.bounds.overlaps(
                    teleport.bounds
                )
            ) {

                if (
                    controlsManager.isMoveUpPressed()
                ) {

                    gameData.spirit =
                        soulManager.getSoul();

                    gameData.mapName =
                        teleport.targetMap;

                    gameData.spawnId =
                        teleport.targetSpawn;

                    game.setScreen(
                        new GameScreen(
                            game,
                            gameData
                        )
                    );

                    return;
                }
            }
        }

        healthHud.update(delta);
        soulAnimator.update(delta);

        float camX =
            player.bounds.x +
                player.bounds.width / 2f;

        float camY =
            player.bounds.y +
                player.bounds.height / 2f;

        camera.position.set(
            Math.round(camX),
            Math.round(camY),
            0f
        );

        clampCameraToMap();

        camera.update();

        if (shakeTime > 0f) {

            shakeTime -= delta;

            camera.position.x +=
                MathUtils.random(
                    -shakeIntensity,
                    shakeIntensity
                );

            camera.position.y +=
                MathUtils.random(
                    -shakeIntensity,
                    shakeIntensity
                );
        }

        ScreenUtils.clear(
            0.15f,
            0.15f,
            0.2f,
            1f
        );

        viewport.apply();

        firstMap.render(camera);
        batch.setProjectionMatrix(
            camera.combined
        );

        batch.begin();

        if (ambientDust != null) {
            ambientDust.render(batch);
        }

        if (ambientLeaves!= null) {
            ambientLeaves.render(batch);
        }

        for (Enemy enemy :
            firstMap.getEnemies()) {

            enemy.render(batch);
        }

        if (firstMap.getZote() != null) {

            firstMap.getZote().render(batch);
        }

        if (cheatManager.shouldTeleportToBoss()) {

            cheatManager.consumeBossTeleport();

            gameData.spirit = soulManager.getCurrentSoul();
            gameData.health = healthHud.getCurrentHealth();

            gameData.mapName = "MAp4";
            gameData.spawnId = "spawn5";

            game.setScreen(new GameScreen(game, gameData));
            return;
        }

        if (cheatManager.shouldFillSoul()) {

            cheatManager.consumeFillSoul();

            soulManager.setSoul(
                SoulManager.MAX_SOUL
            );
        }

        firstMap.renderCharmPickups(batch);
        player.render(batch);
        spellManager.render(batch);
        effectManager.render(batch);

        for (
            SecretMask mask :
            firstMap.getSecretMasks()
        ) {

            mask.render(batch);
        }

        for (
            BreakableWall wall :
            firstMap.getBreakableWalls()
        ) {

            wall.render(batch);
        }
        batch.end();

        batch.setProjectionMatrix(
            hudCamera.combined
        );

        batch.begin();

        batch.draw(
            soulBackground,
            20f,
            950f,
            210f,
            195f
        );
        healthHud.render(batch);
        soulAnimator.render(batch);

        batch.draw(
            souleyes,
            40,
            1020,
            80,
            20
        );

        achievementPopupQueue.render(batch, font, WORLD_WIDTH, WORLD_HEIGHT);

        if (
            dialogueBox != null
                &&
                firstMap.getZote()
                    .getDialogueManager()
                    .isActive()
        ) {

            dialogueBox.render(batch);
        }
        if (firstMap.getZote() != null && firstMap.getZote().canInteract(player) &&
                !firstMap.getZote().getDialogueManager().isActive()) {
            font.draw(batch, "PRESS E", 850, 300);
        }
        batch.end();
    }
    public GameData getGameData() {return gameData;}
    public HealthHud getHealthHud() {return healthHud;}
    private void clampCameraToMap() {
        float halfWidth = camera.viewportWidth * camera.zoom / 2f;
        float halfHeight = camera.viewportHeight * camera.zoom / 2f;
        float minX = halfWidth;
        float minY = halfHeight;
        float maxX = mapWidth - halfWidth;
        float maxY = mapHeight - halfHeight;
        if (mapWidth < halfWidth * 2f) {camera.position.x = mapWidth / 2f;
        } else {camera.position.x = MathUtils.clamp(camera.position.x, minX, maxX);}

        if (mapHeight < halfHeight * 2f) {camera.position.y = mapHeight / 2f;
        } else {camera.position.y = MathUtils.clamp(camera.position.y, minY, maxY);}
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (healthHud != null) healthHud.dispose();
        if (firstMap != null) firstMap.dispose();
        if (font != null) font.dispose();

    }
}



