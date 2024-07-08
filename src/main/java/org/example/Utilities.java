// Don't forget to rename the package
package org.example;

import java.awt.Image;
import javax.swing.ImageIcon;


// Interface for keeping constant values
interface MyConstants
{
    //----- Resource files
    //      Don't forget to change path
    static final String PATH        = "src/main/java/org/example/resources/";
    static final String FILE_BG     = PATH + "Background.png";
    static final String PLAYER = PATH + "crossbow.png";
    static final String SLIME = PATH + "Slime01.png";
    static final String WIZARD = PATH + "IceWizard.png";
    static final String WIZARD_UP = PATH + "IceWizard_Up.png";
    static final String WIZARD_DOWN = PATH + "IceWizard_Down.png";
    static final String ARROW = PATH + "wing.png";
    static final String FHeart = PATH + "FullHeart.png";
    static final String NHeart = PATH + "NoHeart.png";
    static final String FIREBALL = PATH + "RedBall.png";

    //----- Sizes and locations
    static final int FRAMEWIDTH   = 1000;
    static final int FRAMEHEIGHT  = 600;
    static final int GROUND_Y     = 220;
    static final int TOP_LANE     = 80;
    static final int MIDDLE_LANE     = 250;
    static final int BOTTOM_LANE    = 430;
    static final int playerX = 150;
    static final int slimeX = 980;
    static final int slimeWidth = 64;
    static final int slimeHeight = 64;
    static final int slimeSpeed = 10;
    static final int wallX = 100;
}


// Auxiliary class to resize image
class MyImageIcon extends ImageIcon
{
    public MyImageIcon(String fname)  { super(fname); }
    public MyImageIcon(Image image)   { super(image); }

    public MyImageIcon resize(int width, int height)
    {
        Image oldimg = this.getImage();
        Image newimg = oldimg.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new MyImageIcon(newimg);
    }
}