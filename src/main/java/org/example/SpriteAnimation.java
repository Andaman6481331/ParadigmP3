package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;

public class SpriteAnimation extends JPanel implements ActionListener {
    private BufferedImage spriteSheet;
    private BufferedImage[] frames;
    private int currentFrame;
    private Timer timer;
    private int frameWidth;
    private int frameHeight;

    public SpriteAnimation(String spriteSheetPath, int frameWidth, int frameHeight, int numFrames, int delay) {
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        setOpaque(false);
        try {
            spriteSheet = ImageIO.read(new File(spriteSheetPath));
            frames = new BufferedImage[numFrames];
            for (int i = 0; i < numFrames; i++) {
                frames[i] = spriteSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }
            timer = new Timer(delay, this);
            timer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (frames != null && frames.length > 0) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(frames[currentFrame], 0, 0, frameWidth, frameHeight, this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        currentFrame = (currentFrame + 1) % frames.length;
        repaint();
    }

//    public static void main(String[] args) {
//        JFrame frame = new JFrame("Sprite Animation");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(400, 400);
//
//        SpriteAnimationPanel animationPanel = new SpriteAnimationPanel("src/main/java/org/example/resources/Slime01.png", 64, 64, 10, 100);
//        frame.add(animationPanel);
//        frame.setVisible(true);
//    }
}