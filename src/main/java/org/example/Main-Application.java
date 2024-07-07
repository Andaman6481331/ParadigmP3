package org.example;

import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

class MainApplication extends JFrame implements KeyListener {
    private JLabel contentpane;
    private CharacterLabel[] petLabels;
    private CharacterLabel activeLabel;
    private int framewidth = MyConstants.FRAMEWIDTH;
    private int frameheight = MyConstants.FRAMEHEIGHT;
    private int groundY = MyConstants.GROUND_Y;
    private int top_laneY = MyConstants.TOP_LANE;
    private int middle_laneY = MyConstants.MIDDLE_LANE;
    private int bottom_laneY = MyConstants.BOTTOM_LANE;
    private int playerX = MyConstants.playerX;
    private int slimeX = MyConstants.slimeX;
    private int slimeWidth = MyConstants.slimeWidth;
    private int slimeHeight = MyConstants.slimeHeight;

    private Timer slimeTimer;

    public static void main(String[] args) {
        new MainApplication();
    }

    public MainApplication() {
        setSize(framewidth, frameheight);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // set background image by using JLabel as contentpane
        setContentPane(contentpane = new JLabel());
        MyImageIcon background = new MyImageIcon(MyConstants.FILE_BG);
        contentpane.setIcon(background);
        contentpane.setLayout(null);

        petLabels = new CharacterLabel[1];
        petLabels[0] = new CharacterLabel(MyConstants.PLAYER, 100, 100, this);
        petLabels[0].setMoveConditions(playerX, groundY, true, false); // X:150 Y:220

        // Add player to contentpane
        contentpane.add(petLabels[0]);

        setDog();
        repaint();
        addKeyListener(this);

        // Start generating slimes at random intervals
        startSlimeGeneration();
    }

    public CharacterLabel getActiveLabel() { return activeLabel; }
    public void setDog() { activeLabel = petLabels[0]; setTitle("Dog is active"); }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'd':
                setDog();
                break;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                getActiveLabel().moveUp();
                break;
            case KeyEvent.VK_S:
                getActiveLabel().moveDown();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    private void initializeSlime() {
        Random random = new Random();
        int[] lanes = {top_laneY, middle_laneY, bottom_laneY};
        int randomLane = lanes[random.nextInt(3)];

        SpriteAnimation slime = new SpriteAnimation(MyConstants.SLIME, slimeWidth, slimeHeight, 10, 200);
        slime.setStartPosition(slimeX, randomLane);
        slime.startMovement();

        contentpane.add(slime);
        contentpane.repaint();
    }

    private void startSlimeGeneration() {
        slimeTimer = new Timer();
        scheduleNextSlimeGeneration();
    }

    private void scheduleNextSlimeGeneration() {
        int randomDelay = new Random().nextInt(3000) + 1000; // Random delay between 1 and 4 seconds
        slimeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                initializeSlime();
                scheduleNextSlimeGeneration(); // Schedule the next slime generation
            }
        }, randomDelay);
    }
}


////////////////////////////////////////////////////////////////////////////////
abstract class BaseLabel extends JLabel {
    protected MyImageIcon iconMain, iconAlt;
    protected int curX, curY, width, height;
    protected boolean horizontalMove, verticalMove;
    protected MainApplication parentFrame;

    // Constructors
    public BaseLabel(String file1, int w, int h, MainApplication pf) {
        width = w;
        height = h;
        iconMain = new MyImageIcon(file1).resize(width, height);
        setHorizontalAlignment(JLabel.CENTER);
        setIcon(iconMain);
        parentFrame = pf;
        iconAlt = null;
    }

    public BaseLabel(String file1, String file2, int w, int h, MainApplication pf) {
        this(file1, w, h, pf);
        iconAlt = new MyImageIcon(file2).resize(width, height);
    }

    // Common methods
    public void setMoveConditions(boolean hm, boolean vm) {
        horizontalMove = hm;
        verticalMove = vm;
    }

    public void setMoveConditions(int x, int y, boolean hm, boolean vm) {
        curX = x;
        curY = y;
        setBounds(curX, curY, width, height);
        setMoveConditions(hm, vm);
    }

    abstract public void updateLocation();
}

class CharacterLabel extends BaseLabel {
    public CharacterLabel(String file1, int w, int h, MainApplication pf) {
        // Main icon without wings, alternative icon with wings
        super(file1, w, h, pf);
    }

    public void updateLocation() {
        setLocation(curX, curY);
    }

    public void moveUp() {
        if (this.curY == 220) {
            this.curY = 40;
        } else if (this.curY == 400) {
            this.curY = 220;
        }
        updateLocation();
    }

    public void moveDown() {
        if (this.curY == 220) {
            this.curY = 400;
        } else if (this.curY == 40) {
            this.curY = 220;
        }
        updateLocation();
    }
}

