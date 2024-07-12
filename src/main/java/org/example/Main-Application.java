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
    private JLabel timerLabel;
    private int framewidth = MyConstants.FRAMEWIDTH;
    private int frameheight = MyConstants.FRAMEHEIGHT;
    private int top_laneY = MyConstants.TOP_LANE;
    private int middle_laneY = MyConstants.MIDDLE_LANE;
    private int bottom_laneY = MyConstants.BOTTOM_LANE;
    private int playerX = MyConstants.playerX;
    private int slimeX = MyConstants.slimeX;
    private Timer slimeTimer;
    private ArrayList<ArrowAnimation> arrows;
    private ArrayList<SpriteAnimation> slimes;
    private int heartsCount;
    private boolean arrowCooldown = false;
    private final int arrowCooldownDelay = 450; // 0.45 seconds
    Sound sound = new Sound();
    private Point initialClick;
    private JLabel bombLabel;
    private JLabel snowflakeLabel;
    private String playerName;
    private int numofheart;
    private int NumHeart;
    private Timer countdownTimer;
    private int timeSet;

    public static void main(String[] args) {
        new MainApplication("Player", 3,30, MyConstants.FILE_BG1);
    }

    public MainApplication(String playerName, int numheart, int timeSet, String bg) {
        this.playerName = playerName;
        this.numofheart = numheart;
        this.NumHeart = numheart;
        this.timeSet = timeSet;
        setTitle("Slime Slayer69");
        setSize(framewidth, frameheight);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        setContentPane(contentpane = new JLabel());
        MyImageIcon background = new MyImageIcon(bg);
        contentpane.setIcon(background);
        contentpane.setLayout(null);

        Wizard = new PlayerAnimation(MyConstants.WIZARD, MyConstants.WIZARD_UP, MyConstants.WIZARD_DOWN, MyConstants.WIZARD_SHOOT, 128, 128, 6, 4, 4, 5, 200);
        Wizard.setPlayerStart(playerX, middle_laneY - 50);
        contentpane.add(Wizard);

        JLabel nameLabel = new JLabel("Player : " + playerName);
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setFont(new Font("Serif", Font.BOLD, 20));
        nameLabel.setBounds(0,0 , 200, 30); // Adjust the position as needed
        contentpane.add(nameLabel);

        timerLabel = new JLabel("Time: " + timeSet);
        timerLabel.setFont(new Font("Serif", Font.BOLD, 20));
        timerLabel.setForeground(Color.WHITE); // Set the text color
        timerLabel.setBounds(450, 20, 200, 30); // Adjust the position as needed
        contentpane.add(timerLabel);

        repaint();

        hearts = new JLabel[numofheart];
        heartsCount = numofheart; // hearts count
        for (int i = 0; i < hearts.length; i++) {
            hearts[i] = new JLabel(new MyImageIcon(MyConstants.FHeart).resize(25, 21));
            hearts[i].setBounds(830 + (i * 30), 20, 25, 21);
            contentpane.add(hearts[i]);
        }

        JLabel bombLabel = new JLabel(new MyImageIcon(MyConstants.BOMB).resize(64, 64));
        bombLabel.setBounds(23, frameheight - 133, 64, 64); // Adjusted position to be bottom left
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
                int thisX = bombLabel.getLocation().x;
                int thisY = bombLabel.getLocation().y;

                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                bombLabel.setLocation(X, Y);
                //check bomb

                if (bombLabel.getBounds().intersects(Wizard.getBounds())) {
                    Skill();
                    contentpane.remove(bombLabel);
                    contentpane.repaint();
                }
            }
        });

        snowflakeLabel = new JLabel(new MyImageIcon(MyConstants.SNOWFLAKE).resize(64, 64));
        snowflakeLabel.setBounds(21, frameheight - 187, 64, 64); // Adjusted position to be above bomb
        contentpane.add(snowflakeLabel);

        snowflakeLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                getComponentAt(initialClick);
            }
        });

        snowflakeLabel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int thisX = snowflakeLabel.getLocation().x;
                int thisY = snowflakeLabel.getLocation().y;

                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;

                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                snowflakeLabel.setLocation(X, Y);
                //check snowflake
                if (snowflakeLabel.getBounds().intersects(Wizard.getBounds())) {
                    SlowSkill();
                    contentpane.remove(snowflakeLabel);
                    contentpane.repaint();
                }
            }
        });

        repaint();
        addKeyListener(this);

        arrows = new ArrayList<>();
        slimes = new ArrayList<>();

        startSlimeGeneration();

        Timer collisionTimer = new Timer();
        collisionTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkCollisions();
                checkSlimesPosition();
            }
        }, 0, 100);

        startCountdownTimer();
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
                    playSE(1);
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

    public void HeartModify(int numheart){
        this.numofheart = numheart;
    }

    public void TimerCountdown(int timeSet){
        this.timeSet = timeSet;
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
        if (bombLabel != null && bombLabel.getParent() != null) {
            contentpane.remove(bombLabel);
            contentpane.repaint();
            bombLabel = null;
        }
        SkillAnimation skill = new SkillAnimation(MyConstants.LIGHTNING, 640, 64, 10, 100);
        skill.setStartPosition(Wizard.getX() + 80, Wizard.getY() + 30);

        if (Wizard.getY() == top_laneY - 50) {
            for (int i = 0; i < slimes.size(); i++) {
                SpriteAnimation slime = slimes.get(i);
                if (slime.getY() >= top_laneY - 50 && slime.getY() <= top_laneY + 10 && slime.getX() <= 800) {
                    slime.die();
                    slimes.remove(i);
                    i--;
                }
            }
        } else if (Wizard.getY() == middle_laneY - 50) {
            for (int i = 0; i < slimes.size(); i++) {
                SpriteAnimation slime = slimes.get(i);
                if (slime.getY() >= middle_laneY - 50 && slime.getY() <= middle_laneY + 10 && slime.getX() <= 800) {
                    slime.die();
                    slimes.remove(i);
                    i--;
                }
            }
        } else if (Wizard.getY() == bottom_laneY - 50) {
            for (int i = 0; i < slimes.size(); i++) {
                SpriteAnimation slime = slimes.get(i);
                if (slime.getY() >= bottom_laneY - 50 && slime.getY() <= bottom_laneY + 10 && slime.getX() <= 800) {
                    slime.die();
                    slimes.remove(i);
                    i--;
                }
            }
        }

        contentpane.add(skill);
        contentpane.repaint();

        Timer respawnTimer = new Timer();
        respawnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                bombLabel = new JLabel(new MyImageIcon(MyConstants.BOMB).resize(64, 64));
                bombLabel.setBounds(23, frameheight - 133, 64, 64);
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

                        int thisX = bombLabel.getLocation().x;
                        int thisY = bombLabel.getLocation().y;

                        int xMoved = e.getX() - initialClick.x;
                        int yMoved = e.getY() - initialClick.y;

                        int X = thisX + xMoved;
                        int Y = thisY + yMoved;
                        bombLabel.setLocation(X, Y);

                        if (bombLabel.getBounds().intersects(Wizard.getBounds())) {
                            Skill(); // Reactivate skill if bomb dragged onto wizard again
                            contentpane.remove(bombLabel);
                            contentpane.repaint();
                        }
                    }
                });

                contentpane.repaint();
            }
        }, 5000); // Respawn after 5 seconds
    }


    public void SlowSkill() {
        for (SpriteAnimation slime : slimes) {
            slime.setSlowModifier(0.2);
        }


        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (SpriteAnimation slime : slimes) {
                    slime.setSlowModifier(1);
                }
            }
        }, 5000);

        Timer snowflakeRespawnTimer = new Timer();
        snowflakeRespawnTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    snowflakeLabel.setBounds(21, frameheight - 187, 64, 64);
                    contentpane.add(snowflakeLabel);
                    contentpane.repaint();
                });
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
                        contentpane.remove(arrow);
                        arrows.remove(i);
                        i--;
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
    private void startCountdownTimer() {
        countdownTimer = new Timer();
        countdownTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeSet--;
                timerLabel.setText("Time: " + timeSet);
                if (timeSet <= 0) {
                    gameWin();
                }
            }
        }, 1000, 1000); // Update every second
    }

    private void gameOver() {
        countdownTimer.cancel();
        JOptionPane.showMessageDialog(this, "Game Over");
        new StartMenu().setVisible(true);
        dispose();
    }

    private void gameWin() {
        countdownTimer.cancel();
        JOptionPane.showMessageDialog(this, "You Win!");
        new StartMenu().setVisible(true);
        dispose();
    }

    public void playSE(int i) {
        sound.setFile(i);
        sound.play();
    }

}
abstract class BaseLabel extends JLabel {
    protected MyImageIcon iconMain, iconAlt;
    protected int curX, curY, width, height;
    protected boolean horizontalMove, verticalMove;
    protected MainApplication parentFrame;

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
