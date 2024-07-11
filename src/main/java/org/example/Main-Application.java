package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

class MainApplication extends JFrame implements KeyListener {
    private JLabel contentpane;
    private PlayerAnimation Wizard;
    private JLabel[] hearts;
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
    private int slowrate = (int) MyConstants.slowparam;
    private Timer slimeTimer;
    private ArrayList<ArrowAnimation> arrows;
    private ArrayList<SpriteAnimation> slimes;
    private int heartsCount;
    private boolean arrowCooldown = false;
    private final int arrowCooldownDelay = 450; // 0.45 seconds
//    Sound sound = new Sound();
    private Point initialClick;
    private JLabel bombLabel;
    private String playerName;


    public static void main(String[] args) {
        new MainApplication("Player");
    }

    public MainApplication(String playerName) {
        this.playerName = playerName;
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

        // Initialize wizard
        Wizard = new PlayerAnimation(MyConstants.WIZARD, MyConstants.WIZARD_UP, MyConstants.WIZARD_DOWN, MyConstants.WIZARD_SHOOT, 128, 128, 6, 4, 4, 5, 200);
        Wizard.setPlayerStart(playerX, middle_laneY - 50);
        contentpane.add(Wizard);

        // Display player name above wizard
        JLabel nameLabel = new JLabel("Player : " + playerName);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Serif", Font.BOLD, 20));
        nameLabel.setBounds(0,0 , 200, 30); // Adjust the position as needed
        contentpane.add(nameLabel);

        repaint();

        // Initialize hearts
        hearts = new JLabel[3];
        heartsCount = 3; // Initialize hearts count
        for (int i = 0; i < hearts.length; i++) {
            hearts[i] = new JLabel(new MyImageIcon(MyConstants.FHeart).resize(25, 21));
            hearts[i].setBounds(850 + (i * 30), 20, 25, 21);
            contentpane.add(hearts[i]);
        }

        // Add bomb image to bottom left and make it draggable
        JLabel bombLabel = new JLabel(new MyImageIcon(MyConstants.BOMB).resize(64, 64));
        bombLabel.setBounds(10, frameheight - 114, 64, 64); // Adjusted position to be bottom left
        contentpane.add(bombLabel);

        bombLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                getComponentAt(initialClick);
            }
        });

        bombLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // get location of Window
                int thisX = bombLabel.getLocation().x;
                int thisY = bombLabel.getLocation().y;

                // Determine how much the mouse moved since the initial click
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                // Move image to this position
                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                bombLabel.setLocation(X, Y);
                //check bomb
                // Check if bomb is dragged on top of wizard
                if (bombLabel.getBounds().intersects(Wizard.getBounds())) {
                    Skill();
                    contentpane.remove(bombLabel);
                    contentpane.repaint();
                }
            }
        });

        repaint();
        addKeyListener(this);

        arrows = new ArrayList<>();
        slimes = new ArrayList<>();

        // Start generating slimes at random intervals
        startSlimeGeneration();

        // Start a timer to check for collisions and slime positions
        Timer collisionTimer = new Timer();
        collisionTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkCollisions();
                checkSlimesPosition();
            }
        }, 0, 100);
//        playMusic(3);
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
                if (Wizard.getY() == top_laneY - 50 || Wizard.getY() == middle_laneY - 50 || Wizard.getY() == bottom_laneY - 50) {
                    deployArrowWithCooldown();
//                    playSE(1);
//                    Wizard.WizardShoot();
                }
                break;
               
            case KeyEvent.VK_UP:
                if (Wizard.getY() == middle_laneY - 50 || Wizard.getY() == bottom_laneY - 50) {
                    Wizard.WizardmoveUp();
                }
                break;
            case KeyEvent.VK_DOWN:
                if (Wizard.getY() == top_laneY - 50 || Wizard.getY() == middle_laneY - 50) {
                    Wizard.WizardmoveDown();
                }
                break;
            case KeyEvent.VK_F:
                if (Wizard.getY() == top_laneY - 50 || Wizard.getY() == middle_laneY - 50 || Wizard.getY() == bottom_laneY - 50) {
                    SlowSkill();
//                    Wizard.WizardShoot();
                }

                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    private void initializeSlime() {
        Random random = new Random();
        int[] lanes = {top_laneY, middle_laneY, bottom_laneY};
        int randomLane = lanes[random.nextInt(3)];

        SpriteAnimation slime = new SpriteAnimation(MyConstants.SLIME, MyConstants.SLIME_POP, MyConstants.slimeWidth, MyConstants.slimeHeight, 10, 7, 200);
        slime.setStartPosition(slimeX, randomLane);
        slime.startMovement();

        slimes.add(slime);
        contentpane.add(slime);
        contentpane.repaint();
    }

    private void deployArrowWithCooldown() {
        if (!arrowCooldown) {
            deployArrow();
            arrowCooldown = true;
            Timer cooldownTimer = new Timer();
            cooldownTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    arrowCooldown = false;
                }
            }, arrowCooldownDelay);
        }
    }

    private void deployArrow() {
        ArrowAnimation arrow = new ArrowAnimation(MyConstants.FIREBALL, 64, 64, 10, 100);
        arrow.setStartPosition(Wizard.getX() + 20, Wizard.getY() + 30);
        arrow.startMovement_ARROW();

        arrows.add(arrow);
        contentpane.add(arrow);
        contentpane.repaint();
    }

    private void Skill() {
        // Remove bomb if it exists and is visible on the screen
        if (bombLabel != null && bombLabel.getParent() != null) {
            contentpane.remove(bombLabel);
            contentpane.repaint();
            bombLabel = null; // Remove the bomb label from the content pane
        }
//        playSE(0);
        // Perform the skill action (lightning attack)
        SkillAnimation skill = new SkillAnimation(MyConstants.LIGHTNING, 640, 64, 10, 100);
        skill.setStartPosition(Wizard.getX() + 80, Wizard.getY() + 30);

        // Check wizard's position to affect slimes accordingly
        if (Wizard.getY() == top_laneY - 50) {
            for (int i = 0; i < slimes.size(); i++) {
                SpriteAnimation slime = slimes.get(i);
                if (slime.getY() >= top_laneY - 50 && slime.getY() <= top_laneY + 10 && slime.getX() <= 800) {
                    slime.die();
                    slimes.remove(i);
                    i--; // Decrement index to account for removed element
                }
            }
        } else if (Wizard.getY() == middle_laneY - 50) {
            for (int i = 0; i < slimes.size(); i++) {
                SpriteAnimation slime = slimes.get(i);
                if (slime.getY() >= middle_laneY - 50 && slime.getY() <= middle_laneY + 10 && slime.getX() <= 800) {
                    slime.die();
                    slimes.remove(i);
                    i--; // Decrement index to account for removed element
                }
            }
        } else if (Wizard.getY() == bottom_laneY - 50) {
            for (int i = 0; i < slimes.size(); i++) {
                SpriteAnimation slime = slimes.get(i);
                if (slime.getY() >= bottom_laneY - 50 && slime.getY() <= bottom_laneY + 10 && slime.getX() <= 800) {
                    slime.die();
                    slimes.remove(i);
                    i--; // Decrement index to account for removed element
                }
            }
        }

        contentpane.add(skill);
        contentpane.repaint();

        // Schedule the bomb respawn after 5 seconds
        Timer respawnTimer = new Timer();
        respawnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                // Create a new bomb label and add mouse listeners
                bombLabel = new JLabel(new MyImageIcon(MyConstants.BOMB).resize(64, 64));
                bombLabel.setBounds(10, frameheight - 114, 64, 64);
                contentpane.add(bombLabel);

                bombLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        initialClick = e.getPoint();
                        getComponentAt(initialClick);
                    }
                });

                bombLabel.addMouseMotionListener(new MouseMotionAdapter() {
                    @Override
                    public void mouseDragged(MouseEvent e) {
                        // Dragging logic
                        int thisX = bombLabel.getLocation().x;
                        int thisY = bombLabel.getLocation().y;

                        int xMoved = e.getX() - initialClick.x;
                        int yMoved = e.getY() - initialClick.y;

                        int X = thisX + xMoved;
                        int Y = thisY + yMoved;
                        bombLabel.setLocation(X, Y);

                        // Check if bomb is dragged on top of wizard
                        if (bombLabel.getBounds().intersects(Wizard.getBounds())) {
                            Skill(); // Reactivate skill if bomb dragged onto wizard again (optional)
                            contentpane.remove(bombLabel);
                            contentpane.repaint();
                        }
                    }
                });

                contentpane.repaint();
            }
        }, 5000); // Respawn after 5 seconds (5000 milliseconds)
    }


    public void SlowSkill() {
        for (SpriteAnimation slime : slimes) {
            slime.setSlowModifier(0.2);
        }

        // Create a new Timer
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (SpriteAnimation slime : slimes) {
                    slime.setSlowModifier(1);
                }
            }
        }, 5000);
    }

    private void startSlimeGeneration() {
        slimeTimer = new Timer();
        scheduleNextSlimeGeneration();
    }

    private void scheduleNextSlimeGeneration() {
        int randomDelay = new Random().nextInt(1750) + 500; //สุ่ม 0.5- 2 วิ
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
                if (arrow.getX() < 900) {
                    if (arrow.getBounds().intersects(slime.getBounds())) {
                        // Remove arrow from content pane and list
                        contentpane.remove(arrow);
                        arrows.remove(i);
                        i--;
                        // Set the slime to die, but do not remove it immediately
                        slime.die();
                        slimes.remove(j);
                        contentpane.repaint();
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
                // playSE(0);
                contentpane.repaint();
                i--;
            }
        }
    }

    private void decreaseHeart() {
        if (heartsCount > 0) {
//            playSE(0);
            heartsCount--;
            hearts[heartsCount].setIcon(new MyImageIcon(MyConstants.NHeart).resize(25, 21));
            if (heartsCount == 0) {
                JOptionPane.showMessageDialog(this, "Game Over");
//                playSE(2);
                new StartMenu().setVisible(true);
                dispose();
            }
            contentpane.repaint();
        }
    }

//    public void playMusic(int i) {
//        sound.setFile(i);
//        sound.play();
//        sound.loop();
//    }
//
//    public void stopMusic() {
//        sound.stop();
//    }
//
//    public void playSE(int i) {
//        sound.setFile(i);
//        sound.play();
//    }

}
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
