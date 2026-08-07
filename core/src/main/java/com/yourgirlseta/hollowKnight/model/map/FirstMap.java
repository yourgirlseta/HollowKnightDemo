package com.yourgirlseta.hollowKnight.model.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.maps.objects.PointMapObject;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ObjectMap;
import com.yourgirlseta.hollowKnight.Main;
import com.yourgirlseta.hollowKnight.model.charms.CharmPickup;
import com.yourgirlseta.hollowKnight.model.enemy.*;
import com.yourgirlseta.hollowKnight.model.enemy.boss.FalseKnight;
import com.yourgirlseta.hollowKnight.model.enums.CharmType;
import com.yourgirlseta.hollowKnight.model.settingsUtils.SettingsManager;
import com.yourgirlseta.hollowKnight.model.zote.Zote;

public class FirstMap {
    private final TiledMap map;
    private final OrthogonalTiledMapRenderer mapRenderer;
    private final Main game;

    private final Array<Rectangle> groundTiles = new Array<>();
    private final Array<Rectangle> hazards = new Array<>();
    private final Array<SecretMask> secretMasks = new Array<>();
    private final Array<BreakableWall> breakableWalls = new Array<>();
    private final ObjectMap<String, Vector2> spawnPoints = new ObjectMap<>();
    private final Array<Enemy> enemies = new Array<>();
    private Texture breakableWallTexture;
    private final Array<Teleport> teleports = new Array<>();
    private final Array<CharmPickup> charmPickups = new Array<>();
    private Zote zote;
    private FalseKnight falseKnight;
    private String mapName;


    public FirstMap(
        String path,
        SpriteBatch batch,
        Main game
    ) {

        this.mapName =
            path.replace(
                ".tmx",
                ""
            );

        map =
            new TmxMapLoader()
                .load(path);

        mapRenderer =
            new OrthogonalTiledMapRenderer(
                map,
                batch
            );

        breakableWallTexture =
            new Texture(
                "Architecture & Environment/Area specfic architecture/Forgotten Crossroads/break wall sprite sheet.png"
            );

        this.game = game;

        parseObjects();
    }


    public Array<Rectangle> getEnemyCollisionTiles() {

        Array<Rectangle> collisions =
            new Array<>();

        collisions.addAll(groundTiles);

        for (BreakableWall wall :
            breakableWalls) {

            if (wall.isSolid()) {

                collisions.add(
                    wall.bounds
                );
            }
        }

        return collisions;
    }

    private void parseObjects() {
        parseCollisionLayer();
        parseHazardLayer();
        parseSecretMaskLayer();
        parseEnemyLayer();
        parseTeleportLayer();
        parseSpawnLayer();
        parseNpcLayer();
        parseCharmPickupLayer();
    }

    public Array<Enemy> getEnemies() {

        return enemies;
    }

    public FalseKnight getFalseKnight() {
        return falseKnight;
    }

    private void parseCharmPickupLayer() {
        MapLayer layer = map.getLayers().get("charm_pickups");
        if (layer == null) return;

        for (MapObject obj : layer.getObjects()) {
            if (!(obj instanceof RectangleMapObject)) continue;

            Rectangle rect = ((RectangleMapObject) obj).getRectangle();

            String charmName = obj.getProperties().get("charm", String.class);
            if (charmName == null) continue;

            try {
                CharmType type = CharmType.valueOf(charmName);
                Texture texture = new Texture(Gdx.files.internal(type.iconPath));
                charmPickups.add(new CharmPickup(new Rectangle(rect), type, texture));
            } catch (IllegalArgumentException e) {
                System.out.println("Unknown charm name in Tiled: " + charmName);
            }
        }
    }

    public void renderCharmPickups(SpriteBatch batch) {
        for (CharmPickup pickup : charmPickups) {
            if (pickup.collected) continue;

            batch.draw(
                pickup.texture,
                pickup.bounds.x,
                pickup.bounds.y,
                pickup.bounds.width,
                pickup.bounds.height
            );
        }
    }

    public Array<CharmPickup> getCharmPickups() {
        return charmPickups;
    }

    private void parseCollisionLayer() {
        MapLayer collisionLayer = map.getLayers().get("collision");
        if (collisionLayer == null) return;

        for (MapObject obj : collisionLayer.getObjects()) {
            String kind = obj.getProperties().get("kind", String.class);

            if (!(obj instanceof RectangleMapObject)) continue;

            Rectangle rect = ((RectangleMapObject) obj).getRectangle();

            if ("ground".equals(kind) || "wall".equals(kind)) {
                groundTiles.add(new Rectangle(rect));
            } else if ("breakable_wall".equals(kind)) {
                int hp = obj.getProperties().get("hp", 1, Integer.class);
                breakableWalls.add(new BreakableWall(rect.x, rect.y, rect.width, rect.height, hp, breakableWallTexture));
            }
        }

    }

    public Vector2 getPlayerSpawn(
        String spawnId
    ) {

        return new Vector2(
            spawnPoints.get(spawnId)
        );
    }

    private void parseHazardLayer() {
        MapLayer hazardLayer = map.getLayers().get("hazards");
        if (hazardLayer == null) return;

        for (MapObject obj : hazardLayer.getObjects()) {
            if (!(obj instanceof RectangleMapObject)) continue;

            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            hazards.add(new Rectangle(rect));
        }
    }

    private void parseSecretMaskLayer() {
        MapLayer secretLayer = map.getLayers().get("secret");
        if (secretLayer == null) return;

        for (MapObject obj : secretLayer.getObjects()) {
            if (!(obj instanceof RectangleMapObject)) continue;

            Rectangle rect = ((RectangleMapObject) obj).getRectangle();
            boolean visible = obj.getProperties().get("visible", true, Boolean.class);

            SecretMask mask = new SecretMask(rect.x, rect.y, rect.width, rect.height);
            if (!visible) {
                mask.reveal();
            }

            secretMasks.add(mask);
        }
    }

    private void parseSpawnLayer() {

        MapLayer layer =
            map.getLayers().get("spawns");

        if (layer == null)
            return;

        for (MapObject obj :
            layer.getObjects()) {

            if (!(obj instanceof PointMapObject))
                continue;

            String kind =
                obj.getProperties().get(
                    "kind",
                    String.class
                );

            if (
                !"player_spawn".equals(kind)
            ) {
                continue;
            }

            String id =
                obj.getProperties().get(
                    "spawn_id",
                    String.class
                );

            if (id == null) {
                continue;
            }

            PointMapObject point =
                (PointMapObject)obj;

            spawnPoints.put(
                id,
                new Vector2(
                    point.getPoint().x,
                    point.getPoint().y
                )
            );

        }
    }

    private void parseTeleportLayer() {

        MapLayer layer =
            map.getLayers().get("teleports");

        if (layer == null)
            return;

        for (MapObject obj :
            layer.getObjects()) {

            if (!(obj instanceof RectangleMapObject))
                continue;

            Rectangle rect =
                ((RectangleMapObject)obj)
                    .getRectangle();

            MapProperties props =
                obj.getProperties();

            String targetMap =
                props.get(
                    "targetMap",
                    String.class
                );

            String spawn =
                props.get(
                    "spawn",
                    String.class
                );

            teleports.add(
                new Teleport(
                    new Rectangle(rect),
                    targetMap,
                    spawn
                )
            );
        }

    }

    private void parseNpcLayer() {

        MapLayer layer =
            map.getLayers().get("npcs");

        if (layer == null)
            return;

        for (MapObject obj :
            layer.getObjects()) {

            String type =
                obj.getProperties().get(
                    "type",
                    String.class
                );

            if (!"zote".equals(type))
                continue;

            float x = 0f;
            float y = 0f;

            if (obj instanceof PointMapObject) {

                PointMapObject point =
                    (PointMapObject)obj;

                x = point.getPoint().x;
                y = point.getPoint().y;
            }

            this.zote =
                new Zote(
                    x,
                    y,
                    game
                        .getSettingsManager()
                );
        }
    }

    public Zote getZote() {
        return zote;
    }

    public void render(OrthographicCamera camera) {
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    public Array<Rectangle> getGroundTiles() {
        return groundTiles;
    }

    public Array<Rectangle> getHazards() {
        return hazards;
    }

    public Array<SecretMask> getSecretMasks() {
        return secretMasks;
    }

    public Array<BreakableWall> getBreakableWalls() {
        return breakableWalls;
    }

    public void revealMasksForBrokenWalls() {

        for (int i = 0; i < breakableWalls.size; i++) {

            BreakableWall wall = breakableWalls.get(i);

            if (!wall.isBroken()) continue;

            for (int j = 0; j < secretMasks.size; j++) {

                SecretMask mask = secretMasks.get(j);

                if (wall.bounds.overlaps(mask.bounds)) {
                    mask.reveal();
                }
            }
        }
    }

    public float getMapWidth() {
        int width = map.getProperties().get("width", Integer.class);
        int tileWidth = map.getProperties().get("tilewidth", Integer.class);
        return width * tileWidth;
    }

    public float getMapHeight() {
        int height = map.getProperties().get("height", Integer.class);
        int tileHeight = map.getProperties().get("tileheight", Integer.class);
        return height * tileHeight;
    }


    private void parseEnemyLayer() {

        MapLayer layer =
            map.getLayers().get("enemies");

        if (layer == null)
            return;

        for (MapObject obj :
            layer.getObjects()) {

            MapProperties props =
                obj.getProperties();

            String type =
                props.get(
                    "type",
                    String.class
                );

            if (type == null)
                continue;

            float x = 0f;
            float y = 0f;

            if (obj instanceof PointMapObject) {

                PointMapObject point =
                    (PointMapObject)obj;

                x = point.getPoint().x;
                y = point.getPoint().y;
            }

            else if (
                obj instanceof RectangleMapObject
            ) {

                Rectangle rect =
                    ((RectangleMapObject)obj)
                        .getRectangle();

                x = rect.x;
                y = rect.y;
            }

            else {
                continue;
            }

            Enemy enemy = null;

            if ("false_knight".equals(type)) {

                falseKnight = new FalseKnight(x, y, y);
                enemy = falseKnight;
            }

            if ("crawlid".equals(type)) {

                float speed =
                    props.get(
                        "speed",
                        Number.class
                    ).floatValue();

                int hp =
                    props.get(
                        "hp",
                        Number.class
                    ).intValue();

                int direction =
                    props.get(
                        "direction",
                        Number.class
                    ).intValue();

                enemy = new Crawlid(
                    x,
                    y,
                    speed,
                    hp,
                    direction
                );
            }

            if ("mossfly".equals(type)) {

                int hp =
                    props.get(
                        "hp",
                        Number.class
                    ).intValue();

                enemy = new MossFly(
                    x,
                    y,
                    hp
                );
            }


            if ("husk_hornhead".equals(type)) {

                float speed =
                    props.get(
                        "speed",
                        Number.class
                    ).floatValue();

                int hp =
                    props.get(
                        "hp",
                        Number.class
                    ).intValue();

                int direction =
                    props.get(
                        "direction",
                        Number.class
                    ).intValue();

                enemy = new HuskHornhead(
                    x,
                    y,
                    speed,
                    hp,
                    direction
                );
            }

            if ("crystal_guardian".equals(type)) {

                int hp =
                    props.get(
                        "hp",
                        Number.class
                    ).intValue();

                int direction =
                    props.get(
                        "direction",
                        Number.class
                    ).intValue();

                enemy = new CrystalGuardian(
                    x,
                    y,
                    hp,
                    direction
                );
            }

            if (enemy != null) {

                enemies.add(enemy);
            }
        }
    }

    public void respawnEnemies() {
        enemies.clear();
        parseEnemyLayer();
    }

    public Array<Teleport> getTeleports() {

        return teleports;
    }

    public String getMapName() {

        return mapName;
    }

    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        breakableWallTexture.dispose();
        for (CharmPickup pickup : charmPickups) {
            pickup.dispose();
        }
    }
}
