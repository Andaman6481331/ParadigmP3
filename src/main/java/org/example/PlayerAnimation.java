package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;


public class PlayerAnimation extends JPanel implements ActionListener {
    private BufferedImage spriteSheet;
    private BufferedImage[] frames;
    private int currentFrame;
    private Timer animationTimer;
    private Timer movementTimer;
    private int frameWidth;
    private int frameHeight;
    private int moveSpeed;
    private int curX, curY;
    private int targetY;
    private boolean moving = false;

    public PlayerAnimation(String spriteSheetPath, int frameWidth, int frameHeight, int numFrames, int delay) {
        animationTimer = new Timer(delay, this);
        animationTimer.start();
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

        } catch (IOException e) {
            e.printStackTrace();
        }
        // Initialize movement timer
        movementTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                movePlayer();
            }
        });
        movementTimer.start();
    }

    public void updatePlayerXY() {
        setLocation(curX, curY);
    }

    public void setPlayerStart(int x, int y) {
        curX = x;
        curY = y;
        setBounds(x, y, frameWidth, frameHeight);
    }
    public void WizardmoveUp() {
        if (curY == MyConstants.MIDDLE_LANE-50) {
            moving=true;
            targetY = MyConstants.TOP_LANE-50;
        } else if (curY == MyConstants.BOTTOM_LANE-50) {
            moving=true;
            targetY = MyConstants.MIDDLE_LANE-50;
        }
        updatePlayerXY();
    }

    public void WizardmoveDown() {
        if (curY == MyConstants.MIDDLE_LANE-50) {
            moving=true;
            targetY =MyConstants.BOTTOM_LANE-50;
        } else if (curY == MyConstants.TOP_LANE-50) {
            moving=true;
            targetY = MyConstants.MIDDLE_LANE-50;
        }
        updatePlayerXY();
    }

    private void movePlayer() {
        if (moving) {
            if (curY < targetY) {
                curY += moveSpeed;
                if (curY >= targetY) {
                    curY = targetY;
                    moving = false;
                }
            } else if (curY > targetY) {
                curY -= moveSpeed;
                if (curY <= targetY) {
                    curY = targetY;
                    moving = false;
                }
            }
            updatePlayerXY();
        }
    }

//    public void Shooting() {
//        ArrowAnimation arrow = new ArrowAnimation(MyConstants.SLIME, MyConstants.slimeWidth, MyConstants.slimeHeight, 5,100);
//        arrow.setStartPosition(this.getX(), this.getY());
//        arrow.startMovement_ARROW();
//
//        arrows.add(arrow);
//        contentpane.add(arrow);
//        contentpane.repaint();
//    }

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