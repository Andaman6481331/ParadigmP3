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
    private BufferedImage idleSpriteSheet;
    private BufferedImage movingUpSpriteSheet;
    private BufferedImage movingDownSpriteSheet;
    private BufferedImage ShootingSpriteSheet;
    private BufferedImage[] idleFrames;
    private BufferedImage[] movingUpFrames;
    private BufferedImage[] movingDownFrames;
    private BufferedImage[] shootingFrames;
    private BufferedImage[] currentFrames;
    private int currentFrame;
    private Timer animationTimer;
    private Timer movementTimer;
    private int frameWidth;
    private int frameHeight;
    private int moveSpeed;
    private int curX, curY;
    private int targetY;
    private boolean moving = false;

    public PlayerAnimation(String idleSpriteSheetPath, String movingUpSpriteSheetPath, String movingDownSpriteSheetPath, String ShootingSpriteSheetPath,int frameWidth, int frameHeight, int numFramesIdle, int numFramesMovingUp, int numFramesMovingDown,int numFramesShooting, int delay) {
        animationTimer = new Timer(delay, this);
        animationTimer.start();
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.moveSpeed = MyConstants.slimeSpeed;
        setOpaque(false);
        try {
            idleSpriteSheet = ImageIO.read(new File(idleSpriteSheetPath));
            idleFrames = new BufferedImage[numFramesIdle];
            for (int i = 0; i < numFramesIdle; i++) {
                idleFrames[i] = idleSpriteSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }

            // Load moving up sprite sheet and frames
            movingUpSpriteSheet = ImageIO.read(new File(movingUpSpriteSheetPath));
            movingUpFrames = new BufferedImage[numFramesMovingUp];
            for (int i = 0; i < numFramesMovingUp; i++) {
                movingUpFrames[i] = movingUpSpriteSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }

            // Load moving down sprite sheet and frames
            movingDownSpriteSheet = ImageIO.read(new File(movingDownSpriteSheetPath));
            movingDownFrames = new BufferedImage[numFramesMovingDown];
            for (int i = 0; i < numFramesMovingDown; i++) {
                movingDownFrames[i] = movingDownSpriteSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }

            ShootingSpriteSheet = ImageIO.read(new File(ShootingSpriteSheetPath));
            shootingFrames = new BufferedImage[numFramesShooting];
            for (int i = 0; i < numFramesMovingDown; i++) {
                shootingFrames[i] = ShootingSpriteSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }

            currentFrames = idleFrames;

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
        currentFrames = movingUpFrames;
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
        currentFrames = movingDownFrames;
    }

    public BufferedImage[] getShootingFrames() {
        return shootingFrames;
    }

    public BufferedImage[] getCurrentFrames() {
        return currentFrames;
    }

    private void movePlayer() {
        if (moving) {
            if (curY < targetY) {
                curY += moveSpeed;
                if (curY >= targetY) {
                    curY = targetY;
                    moving = false;
                    currentFrames = idleFrames;
                }
            } else if (curY > targetY) {
                curY -= moveSpeed;
                if (curY <= targetY) {
                    curY = targetY;
                    moving = false;
                    currentFrames = idleFrames;
                }
            }
            updatePlayerXY();
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (currentFrames != null && currentFrames.length > 0) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.drawImage(currentFrames[currentFrame], 0, 0, frameWidth, frameHeight, this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        currentFrame = (currentFrame + 1) % currentFrames.length;
        repaint();
    }
}