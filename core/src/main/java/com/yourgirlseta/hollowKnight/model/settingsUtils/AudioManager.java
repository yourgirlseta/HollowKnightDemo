package com.yourgirlseta.hollowKnight.model.settingsUtils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.TimeUtils;

public class AudioManager {
    private Music currentMusic;

    private Sound nailSlashSound;
    private Sound enemyHitSound;
    private Sound playerHitSound;
    private Sound wallBreakSound;
    private Sound focusSound;
    private Sound soulSound;
    private Array<Sound> zoteVoices;
    private final ObjectMap<Sound, Long> lastPlayTimes =
        new ObjectMap<>();

    private float musicVolume = 1f;
    private boolean musicMuted = false;

    private float sfxVolume = 1f;

    private boolean sfxMuted = false;

    public AudioManager () {

        nailSlashSound =
            Gdx.audio.newSound(
                Gdx.files.internal(
                    "songs/hero_dream_nail_slash_only.wav"
                )
            );


        playerHitSound =
            Gdx.audio.newSound(
                Gdx.files.internal(
                    "songs/audio (online-video-cutter.com) (1).mp3"
                )
            );

        wallBreakSound =
            Gdx.audio.newSound(
                Gdx.files.internal(
                    "songs/break_wall_after_tutorial_area.wav"
                )
            );

        focusSound =
            Gdx.audio.newSound(
                Gdx.files.internal(
                    "songs/focus.mp3"
                )
            );

        soulSound =
            Gdx.audio.newSound(
                Gdx.files.internal(
                    "songs/soulFill.mp3"
                )
            );

        zoteVoices =
            new Array<>();

        zoteVoices.add(
            Gdx.audio.newSound(
                Gdx.files.internal(
                    "songs/Zote_01.wav"
                )
            )
        );

        zoteVoices.add(
            Gdx.audio.newSound(
                Gdx.files.internal(
                    "songs/Zote_02.wav"
                )
            )
        );

        zoteVoices.add(
            Gdx.audio.newSound(
                Gdx.files.internal(
                    "songs/Zote_03.wav"
                )
            )
        );

        zoteVoices.add(
            Gdx.audio.newSound(
                Gdx.files.internal(
                    "songs/Zote_04.wav"
                )
            )
        );

        zoteVoices.add(
            Gdx.audio.newSound(
                Gdx.files.internal(
                    "songs/Zote_complain_combined.wav"
                )
            )
        );
    }

    public void playSlashSound() {

        if (sfxMuted) {
            return;
        }

        nailSlashSound.play(sfxVolume);
    }


    public void playPlayerHit() {

        if (sfxMuted)
            return;

        long id =
            playerHitSound.play(
                MathUtils.random(
                    0.75f,
                    0.9f
                ) * sfxVolume
            );

        playerHitSound.setPitch(
            id,
            MathUtils.random(
                0.97f,
                1.03f
            )
        );
    }

    public void playFocus() {

        if (sfxMuted) {
            return;
        }

        focusSound.play(sfxVolume);
    }

    public void playSoul() {

        if (sfxMuted) {
            return;
        }

        soulSound.play(sfxVolume);
    }

    public void playWallBreak() {

        if (sfxMuted) {
            return;
        }

        wallBreakSound.play(sfxVolume);
    }

    public void playRandomZoteVoice() {

        if (
            sfxMuted
                ||
                zoteVoices.size == 0
        ) {
            return;
        }

        int index =
            MathUtils.random(
                zoteVoices.size - 1
            );

        zoteVoices.get(index)
            .play(sfxVolume);
    }

    public void playMusic(Music music, boolean looping) {
        if (music == null) return;

        if (currentMusic != null && currentMusic != music) {
            currentMusic.stop();
        }

        currentMusic = music;
        currentMusic.setLooping(looping);
        updateMusicVolume();

        if (!currentMusic.isPlaying()) {
            currentMusic.play();
        }
    }

    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic = null;
        }
    }

    public void setMusicVolume(float musicVolume) {
        this.musicVolume = clamp01(musicVolume);
        updateMusicVolume();
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public void setMusicMuted(boolean musicMuted) {
        this.musicMuted = musicMuted;
        updateMusicVolume();
    }

    public void setSfxVolume(float sfxVolume) {

        this.sfxVolume =
            clamp01(sfxVolume);
    }

    public float getSfxVolume() {

        return sfxVolume;
    }

    public void setSfxMuted(boolean sfxMuted) {

        this.sfxMuted = sfxMuted;
    }

    public boolean isSfxMuted() {

        return sfxMuted;
    }

    public boolean isMusicMuted() {
        return musicMuted;
    }

    private void updateMusicVolume() {
        if (currentMusic == null) {
            return;
        }

        float finalVolume = musicMuted ? 0f : musicVolume;
        currentMusic.setVolume(finalVolume);
    }

    private boolean canPlay(
        Sound sound,
        long cooldown
    ) {

        long now = TimeUtils.millis();

        Long lastTime =
            lastPlayTimes.get(sound);

        if (
            lastTime != null
                &&
                now - lastTime < cooldown
        ) {

            return false;
        }

        lastPlayTimes.put(
            sound,
            now
        );

        return true;
    }

    private float clamp01(float value) {
        if (value < 0f) return 0f;
        if (value > 1f) return 1f;
        return value;
    }
}

