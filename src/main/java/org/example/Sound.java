package org.example;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import java.io.IOException;
import java.net.URL;

public class Sound {
    Clip clip;
    String path = "/org/example/sound/";
    URL[] soundURL = new URL[30];

    public Sound() {
        soundURL[0] = getClass().getResource(path + "Explode.mp3"); //change .mp3 to .wav
        soundURL[1] = getClass().getResource(path + "Fireball.wav");
        soundURL[2] = getClass().getResource(path + "GameOver.mp3");
        soundURL[3] = getClass().getResource(path + "Song.mp3");
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (IOException | LineUnavailableException | javax.sound.sampled.UnsupportedAudioFileException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        clip.start();
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }
}
