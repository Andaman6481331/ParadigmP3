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
    private int moveSpeed;
    private int  curX, curY;
    private int wallX;

    public SpriteAnimation(String spriteSheetPath, int frameWidth, int frameHeight, int numFrames, int delay) {
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
            timer = new Timer(delay, this);
            timer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void updateXY(){
        setLocation(curX, curY);
    }

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
//                    else if (statecount==9) {
//                        curY -=moveSpeed+1;
//
//                    }

                    statecount++;
                    updateXY();
                } else {
                    stopMovement(); // Stop the timer when reached the stopping point
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