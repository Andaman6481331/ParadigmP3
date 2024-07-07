package org.example;

import javax.swing.*;
import java.awt.event.*;

class MainApplication extends JFrame implements KeyListener
{
    private JLabel          contentpane;
    private CharacterLabel  []petLabels;
    private CharacterLabel  activeLabel;
    private SpriteAnimation slime1;
    private SpriteAnimation slime2;
    private SpriteAnimation slime3;
    private int framewidth   = MyConstants.FRAMEWIDTH;
    private int frameheight  = MyConstants.FRAMEHEIGHT;
    private int groundY      = MyConstants.GROUND_Y;
    private int top_laneY      = MyConstants.TOP_LANE;
    private int middle_laneY      = MyConstants.MIDDLE_LANE;
    private int bottom_laneY      = MyConstants.BOTTOM_LANE;
    private int playerX = MyConstants.playerX;
    private int slimeX = MyConstants.slimeX;
    private int slimeWidth = MyConstants.slimeWidth;
    private int slimeHeight = MyConstants.slimeHeight;

    public static void main(String[] args)
    {
        new MainApplication();
    }

    public MainApplication()
    {
        setSize(framewidth, frameheight);
        setLocationRelativeTo(null);
        setVisible(true);
        setDefaultCloseOperation( WindowConstants.EXIT_ON_CLOSE );

        // set background image by using JLabel as contentpane
        setContentPane(contentpane = new JLabel());
        MyImageIcon background = new MyImageIcon(MyConstants.FILE_BG);
        contentpane.setIcon( background );
        contentpane.setLayout( null );

        petLabels = new CharacterLabel[2];
        petLabels[0] = new CharacterLabel(MyConstants.PLAYER,
                100, 100, this);
        petLabels[0].setMoveConditions(playerX, groundY, true, false);   //X:150 Y:220

//==================================================================initialize slimes==================================================================
        slime1 = new SpriteAnimation(MyConstants.SLIME, 64, 64, 10, 200);
        slime1.setStartPosition(slimeX, top_laneY);
        slime1.startMovement();

        slime2 = new SpriteAnimation(MyConstants.SLIME, 64, 64, 10, 200);
        slime2.setStartPosition(slimeX, middle_laneY);
        slime2.startMovement();

        slime3 = new SpriteAnimation(MyConstants.SLIME, 64, 64, 10, 200);
        slime3.setStartPosition(slimeX, bottom_laneY);
        slime3.startMovement();

        //==================================================================Add to contentpane==================================================================
        // first added label is at the front, last added label is at the back
        contentpane.add(slime1);
        contentpane.add(slime2);
        contentpane.add(slime3);
        contentpane.add( petLabels[0] );

        setDog();
        repaint();
        addKeyListener(this);
    }


    public CharacterLabel getActiveLabel()  { return activeLabel; }
    public void setDog()                    { activeLabel = petLabels[0]; setTitle("Dog is active"); }


    @Override
    public void keyPressed(KeyEvent e){
        switch (e.getKeyChar()){
            case 'd' :
                setDog();
                break;
        }
        switch (e.getKeyCode()){
            case KeyEvent.VK_W:
                getActiveLabel().moveUp();
                break;
            case KeyEvent.VK_S:
                getActiveLabel().moveDown();
                break;
        }
    }
    @Override
    public void keyReleased(KeyEvent e){
    }
    @Override
    public void keyTyped(KeyEvent e){
    }

}

////////////////////////////////////////////////////////////////////////////////
abstract class BaseLabel extends JLabel
{
    protected MyImageIcon      iconMain, iconAlt;
    protected int              curX, curY, width, height;
    protected boolean          horizontalMove, verticalMove;
    protected MainApplication  parentFrame;

    // Constructors
    public BaseLabel(String file1, int w, int h, MainApplication pf)
    {
        width = w; height = h;
        iconMain = new MyImageIcon(file1).resize(width, height);
        setHorizontalAlignment(JLabel.CENTER);
        setIcon(iconMain);
        parentFrame = pf;
        iconAlt = null;
    }
    public BaseLabel(String file1, String file2, int w, int h, MainApplication pf)
    {
        this(file1, w, h, pf);
        iconAlt = new MyImageIcon(file2).resize(width, height);
    }

    // Common methods
    public void setMoveConditions(boolean hm, boolean vm)
    {
        horizontalMove = hm;
        verticalMove   = vm;
    }
    public void setMoveConditions(int x, int y, boolean hm, boolean vm)
    {
        curX = x; curY = y;
        setBounds(curX, curY, width, height);
        setMoveConditions(hm, vm);
    }

    abstract public void updateLocation();
}

////////////////////////////////////////////////////////////////////////////////
class CharacterLabel extends BaseLabel
{
    public CharacterLabel(String file1, int w, int h, MainApplication pf)
    {
        // Main icon without wings, alternative icon with wings
        super(file1, w, h, pf);
    }

    public void updateLocation()    {
        setLocation(curX,curY);
    }
    public void moveUp(){
        if(this.curY == 220) {
            this.curY = 40;
        } else if (this.curY == 400) {
            this.curY = 220;
        };
        updateLocation();
    }

    public void moveDown(){
        if(this.curY == 220) {
            this.curY = 400;
        } else if (this.curY == 40) {
            this.curY = 220;
        };
        updateLocation();
    }
}
////////////////////////////////////////////////////////////////////////////////
//class ItemLabel extends BaseLabel implements MouseListener,MouseMotionListener
//{
//    public ItemLabel(String file, int w, int h, MainApplication pf)
//    {
//        // Alternative icon = null
//        super(file, w, h, pf);
//        addMouseListener( this );
//        addMouseMotionListener( this );
//    }
//    private boolean drag = false;
//
//    public void updateLocation()    { setLocation(curX, curY); }
//    public void setMainIcon()       { setIcon(iconMain); }
//    public void setAltIcon()        { setIcon(iconAlt); }
//
//
//    //for MouseListener
//    @Override
//    public void mouseDragged(MouseEvent e) {
//        if(drag){
//            this.curX = this.curX + e.getX();
//            this.curY = this.curY + e.getY();
//
//            Container parent = getParent();
//
//            if (curX < 0)
//                curX = 0;
//            if (curY < 0)
//                curY = 0;
//            if (curX + width  > parent.getWidth())
//                curX = parent.getWidth() - width;
//            if (curY + height > parent.getHeight())
//                curY = parent.getHeight() - height;
//
//            updateLocation();
//
//        }
//    }
//
//    @Override
//    public void mouseMoved(MouseEvent e) {
//    }
//    @Override
//    public void mouseClicked(MouseEvent e) {
//    }
//    @Override
//    public void mousePressed(MouseEvent e) {
//        drag = true;
//    }
//    @Override
//    public void mouseReleased(MouseEvent e) {
//        drag = false;
//    }
//    @Override
//    public void mouseEntered(MouseEvent e) {
//    }
//    @Override
//    public void mouseExited(MouseEvent e) {
//    }
//}
