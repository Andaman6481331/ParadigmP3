package org.example;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

class MainApplication extends JFrame implements KeyListener {
    private JLabel contentpane;
    private PlayerAnimation Wizard;
    private JLabel[] hearts;
//    private Heart FHeart;
    // private SpriteAnimation FHeart1;
    // private SpriteAnimation FHeart2;
    // private SpriteAnimation FHeart3;
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
    private ArrayList<ArrowAnimation> arrows;
    private ArrayList<SpriteAnimation> slimes;
    private int heartsCount;

    public static void main(String[] args) {
        new MainApplication();
    }

    public MainApplication() {
        setTitle("Slime Slayer69");
        setSize(framewidth, frameheight);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // set background image by using JLabel as contentpane
        setContentPane(contentpane = new JLabel());
        MyImageIcon background = new MyImageIcon(MyConstants.FILE_BG);
        contentpane.setIcon(background);
        contentpane.setLayout(null);


//=====================Herb's Test PlyerAnimation Class: plz dont delete these three line==========================
        Wizard = new PlayerAnimation(MyConstants.WIZARD,MyConstants.WIZARD_UP, MyConstants.WIZARD_DOWN,128,128,6,4,4,500);
        Wizard.setPlayerStart(playerX,middle_laneY-50);
        contentpane.add(Wizard);
        repaint();

        //Try change number of hearts on line 57,58 //There is a problem with if too many hearts will create heart image out of the frame try more than 4
        hearts = new JLabel[3];
        heartsCount = 3; // Initialize hearts count
        for (int i = 0; i < hearts.length; i++) {
            hearts[i] = new JLabel(new MyImageIcon(MyConstants.FHeart).resize(25, 21));
            hearts[i].setBounds(850 + (i*30), 20, 25, 21);
            contentpane.add(hearts[i]);
        }


        repaint();
        addKeyListener(this);

        arrows = new ArrayList<>();
        slimes = new ArrayList<>();

        // Start generating slimes at random intervals
        startSlimeGeneration();

        // Start a timer to check for collisions and slime postions
        Timer collisionTimer = new Timer();
        collisionTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkCollisions();
                checkSlimesPosition();
            }
        }, 0, 100);
    }

    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                Wizard.WizardmoveUp();
                break;
            case KeyEvent.VK_S:
                Wizard.WizardmoveDown();
                break;
            case KeyEvent.VK_SPACE:
                if (Wizard.getY()==top_laneY-50||Wizard.getY()==middle_laneY-50||Wizard.getY()==bottom_laneY-50)
                deployArrow();
                break;
            case KeyEvent.VK_UP:
                Wizard.WizardmoveUp();
                break;
            case KeyEvent.VK_DOWN:
                Wizard.WizardmoveDown();
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

        SpriteAnimation slime = new SpriteAnimation(MyConstants.SLIME, MyConstants.slimeWidth, MyConstants.slimeHeight, 10, 200);
        slime.setStartPosition(slimeX, randomLane);
        slime.startMovement();

        slimes.add(slime);
        contentpane.add(slime);
        contentpane.repaint();
    }

    private void deployArrow() {
        ArrowAnimation arrow = new ArrowAnimation(MyConstants.FIREBALL, 64, 64, 10,100);
        arrow.setStartPosition(Wizard.getX()+20, Wizard.getY()+30);
        arrow.startMovement_ARROW();

        arrows.add(arrow);
        contentpane.add(arrow);
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

    private void checkCollisions() {
        for (int i = 0; i < arrows.size(); i++) {
            ArrowAnimation arrow = arrows.get(i);
            for (int j = 0; j < slimes.size(); j++) {
                SpriteAnimation slime = slimes.get(j);
                if (arrow.getX()<950){
                    if (arrow.getBounds().intersects(slime.getBounds())) {
                        // Remove arrow and slime from content pane and lists
                        contentpane.remove(arrow);
                        contentpane.remove(slime);
                        arrows.remove(i);
                        slimes.remove(j);
                        contentpane.repaint();
                        i--;
                        j--;
                        break;
                    }
                }

            }
        }
    }
    private void checkSlimesPosition() {
        for (int i = 0; i < slimes.size(); i++) {
            SpriteAnimation slime = slimes.get(i);
            if (slime.getX() <= 100) {
                // Slime hits the wall, decrease hearts
                decreaseHeart();
                contentpane.remove(slime);
                slimes.remove(i);
                contentpane.repaint();
                i--;
            }
        }
    }

    private void decreaseHeart() {
        if (heartsCount > 0) {
            heartsCount--;
            hearts[heartsCount].setIcon(new MyImageIcon(MyConstants.NHeart).resize(25, 21));
            if (heartsCount == 0) {
                JOptionPane.showMessageDialog(this, "Game Over");
                new StartMenu().setVisible(true);
                dispose();
                
            }
            contentpane.repaint();
        }
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
