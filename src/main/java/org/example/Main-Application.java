package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class MainApplication extends JFrame implements KeyListener
{
    private JLabel          contentpane;
    private CharacterLabel  []petLabels;
    private CharacterLabel  activeLabel;
    private ItemLabel       wingLabel;
    private int framewidth   = MyConstants.FRAMEWIDTH;
    private int frameheight  = MyConstants.FRAMEHEIGHT;
    private int groundY      = MyConstants.GROUND_Y;
    private int skyY         = MyConstants.SKY_Y;
    private int bridgeLeftX  = MyConstants.BRIDGE_LEFT;
    private int bridgeRightX = MyConstants.BRIDGE_RIGHT;

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
        petLabels[0] = new CharacterLabel(MyConstants.FILE_DOG_1, MyConstants.FILE_DOG_2,
                120, 100, this);
        petLabels[0].setMoveConditions(bridgeLeftX-120, groundY, true, false);

        wingLabel = new ItemLabel(MyConstants.FILE_WING, 100, 80, this);
        wingLabel.setMoveConditions(bridgeRightX+300, skyY, true, true);

        // first added label is at the front, last added label is at the back
        contentpane.add( wingLabel );
        contentpane.add( petLabels[0] );

        setDog();
        repaint();
        addKeyListener(this);
    }


    public CharacterLabel getActiveLabel()  { return activeLabel; }
    public void setDog()                    { activeLabel = petLabels[0]; setTitle("Dog is active"); }

    public ItemLabel getWings(){
        return wingLabel;
    }

    @Override
    public void keyPressed(KeyEvent e){
        switch (e.getKeyChar()){
            case 'd' :
                setDog();
                break;

            case 'j':
                if(!getActiveLabel().isHasWings()){
                    getActiveLabel().jump();
                    break;
                }
        }
        switch (e.getKeyCode()){
            case KeyEvent.VK_UP:
                getActiveLabel().moveUp();
                break;
            case KeyEvent.VK_DOWN:
                getActiveLabel().moveDown();
                break;
            case KeyEvent.VK_ESCAPE:
                getActiveLabel().removeWings();
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
    public BaseLabel() { }
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
    public CharacterLabel(String file1, String file2, int w, int h, MainApplication pf)
    {
        // Main icon without wings, alternative icon with wings
        super(file1, file2, w, h, pf);
    }

    public void updateLocation()    {
        setLocation(curX,curY);
        if(this.wings != null){
            wings.setLocation(curX + width / 2 - wings.getWidth() / 2, curY - wings.getHeight() / 2);
        }
    }
    public void moveUp(){
        this.curY -= 10;
        if(this.curY <= 0)
            this.curY = MyConstants.FRAMEWIDTH - 120;
        updateLocation();
    }

    public void moveDown(){
        this.curY += 10;
        if(this.curY >= MyConstants.FRAMEWIDTH - 120)
            this.curY = 0;
        updateLocation();
    }
    // private boolean facingLeft = true;

    public void jump(){
        if(this.curX < MyConstants.BRIDGE_LEFT-110)
            this.curX = MyConstants.BRIDGE_RIGHT+20;
        else if(this.curX > MyConstants.BRIDGE_RIGHT)
            this.curX = MyConstants.BRIDGE_LEFT-120;
        else
            return;
        updateLocation();
    }
    //Wing Property Functions
    //set initial state
    private boolean hasWings = false;
    private ItemLabel wings;

    public boolean isHasWings(){
        return hasWings;
    }
    public void putWings(ItemLabel wings){
        if(!hasWings){
            this.wings = wings;     //put on wings
            hasWings = true;        //set property
            setIcon(iconAlt);
            parentFrame.remove(wings);
            parentFrame.getWings().setVisible(false);   //hide wings component from view of GUI
            updateLocation();
        }
    }
    public void removeWings(){
        if (hasWings) {
            hasWings = false;       //disable the up and down function
            setIcon(iconMain);      //change sprite
            parentFrame.add(wings);
            parentFrame.getWings().setVisible(true);
            parentFrame.getActiveLabel().curY = MyConstants.GROUND_Y;
            updateLocation();
            parentFrame.getWings().setLocation(800, 50); // Set the location of wings
            this.wings = null;      //for some reason need to be after updateLocation()
        }
    }
}

////////////////////////////////////////////////////////////////////////////////
class ItemLabel extends BaseLabel implements MouseListener,MouseMotionListener
{
    public ItemLabel(String file, int w, int h, MainApplication pf)
    {
        // Alternative icon = null
        super(file, w, h, pf);
        addMouseListener( this );
        addMouseMotionListener( this );
    }
    private boolean drag = false;

    public void updateLocation()    { setLocation(curX, curY); }
    public void setMainIcon()       { setIcon(iconMain); }
    public void setAltIcon()        { setIcon(iconAlt); }


    //for MouseListener
    @Override
    public void mouseDragged(MouseEvent e) {
        if(drag){
            this.curX = this.curX + e.getX();
            this.curY = this.curY + e.getY();

            Container parent = getParent();

            if (curX < 0)
                curX = 0;
            if (curY < 0)
                curY = 0;
            if (curX + width  > parent.getWidth())
                curX = parent.getWidth() - width;
            if (curY + height > parent.getHeight())
                curY = parent.getHeight() - height;

            updateLocation();

            if(this.getBounds().intersects((parentFrame.getActiveLabel().getBounds()))){
                parentFrame.getActiveLabel().putWings(parentFrame.getWings());
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
    @Override
    public void mouseClicked(MouseEvent e) {
    }
    @Override
    public void mousePressed(MouseEvent e) {
        drag = true;
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        drag = false;
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }
}
