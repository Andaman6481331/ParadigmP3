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
    private BufferedImage popingSheet;
    private BufferedImage[] frames;
    private BufferedImage[] popframes;
    private BufferedImage[] currentFrames;
    private int currentFrame;
    private Timer timer;
    private int frameWidth;
    private int frameHeight;
    private int moveSpeed;
    private int curX, curY;
    private int wallX;
    private boolean die = false;

    public SpriteAnimation(String spriteSheetPath, String popingSheetPath,int frameWidth, int frameHeight, int numFrames, int numFramesPop,int delay) {
        timer = new Timer(delay, this);
        timer.start();
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.moveSpeed = MyConstants.slimeSpeed;
        this.wallX = MyConstants.wallX;
        setOpaque(false);
        try {
            spriteSheet = ImageIO.read(new File(spriteSheetPath));
            frames = new BufferedImage[numFrames];
            for (int i = 0; i < numFrames; i++) {
                frames[i] = spriteSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }

            popingSheet = ImageIO.read(new File(popingSheetPath));
            popframes = new BufferedImage[numFramesPop];
            for (int i = 0; i < numFramesPop; i++) {
                popframes[i] = popingSheet.getSubimage(i * frameWidth, 0, frameWidth, frameHeight);
            }

            currentFrames = frames;

//            timer = new Timer(delay, this);
//            timer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void updateXY() {
        setLocation(curX, curY);
    }
    public int getY() { return curY;}
    public int getX() { return curX;}

    public void setStartPosition(int x, int y) {
        curX = x;
        curY = y;
        setBounds(x, y, frameWidth, frameHeight);
    }

    public void startMovement() {
        timer = new Timer(100, new ActionListener() {
            private int statecount = 1;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (curX > wallX) {
                    if (statecount>12){
                        statecount=0;
                    } else if (statecount>=2 && statecount<=4) {
                        curY -=moveSpeed*4/3;
                        curX -= moveSpeed;
                    } else if (statecount>=5 && statecount<=8) {
                        curY +=moveSpeed*3/3;
                        curX -= moveSpeed;
                    }
                    statecount++;
                    updateXY();
                } else {
                    // Remove the slime from the parent container and stop the timer
                    Container parent = getParent();
                    if (parent != null) {
                        parent.remove(SpriteAnimation.this);
                        parent.repaint();
                    }
                    stopMovement(); // Stop the timer
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

    public void die(){
        if (!die) {
            die = true;
            stopMovement();
            currentFrames = popframes;
            currentFrame = 0;
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

        if (die && currentFrame == currentFrames.length-1) {
            Container parent = getParent();
            if (parent != null) {
                parent.remove(SpriteAnimation.this);
                parent.repaint();
            }

        }

    }
}