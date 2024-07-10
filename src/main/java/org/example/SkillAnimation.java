package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;

public class SkillAnimation extends JPanel implements ActionListener {
    private BufferedImage spriteSheet;
    private BufferedImage[] frames;
    private int currentFrame;
    private Timer timer;
    private int frameWidth;
    private int frameHeight;
    private int moveSpeed;
    private int curX, curY;

    public SkillAnimation(String spriteSheetPath, int frameWidth, int frameHeight, int numFrames, int delay) {
        timer = new Timer(delay, this);
        timer.start();
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.moveSpeed = MyConstants.slimeSpeed;
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

    public void updateXY() {
        setLocation(curX, curY);
    }
    public void setStartPosition(int x, int y) {
        curX = x;
        curY = y;
        setBounds(x, y, frameWidth, frameHeight);
    }

    public void startMovement_ARROW() {
        timer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (curX < 950) {
                    curX += moveSpeed*1.5;
                    updateXY();
                } else {
                    // Remove the slime from the parent container and stop the timer
                    stopMovement(); // Stop the timer
                    Container parent = getParent();
                    if (parent != null) {
                        parent.remove(SkillAnimation.this);
                        parent.repaint();
                    }
                }
            }
        });
        timer.start();
    }
    public void stopMovement() {
        if (timer != null) {
            timer.stop();
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
}
