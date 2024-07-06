package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Animation {
    public void getSlimeAni(){
        try{
            BufferedImage stand1 =ImageIO.read(getClass().getResourceAsStream("/Slime01/Stand1.png"));
            BufferedImage stand2 =ImageIO.read(getClass().getResourceAsStream("/Slime01/Stand2.png"));
            BufferedImage stand3 =ImageIO.read(getClass().getResourceAsStream("/Slime01/Stand3.png"));
            BufferedImage stand4 =ImageIO.read(getClass().getResourceAsStream("/Slime01/Stand4.png"));
            BufferedImage stand5 =ImageIO.read(getClass().getResourceAsStream("/Slime01/Stand5.png"));
            BufferedImage stand6 =ImageIO.read(getClass().getResourceAsStream("/Slime01/Stand6.png"));
            BufferedImage stand7 =ImageIO.read(getClass().getResourceAsStream("/Slime01/Stand7.png"));
            BufferedImage stand8 =ImageIO.read(getClass().getResourceAsStream("/Slime01/Stand8.png"));
            BufferedImage stand9 =ImageIO.read(getClass().getResourceAsStream("/Slime01/Stand9.png"));
            BufferedImage stand10 =ImageIO.read(getClass().getResourceAsStream("/Slime01/Stand10.png"));
            BufferedImage stand11 =ImageIO.read(getClass().getResourceAsStream("/Slime01/Stand11.png"));
            BufferedImage stand12 =ImageIO.read(getClass().getResourceAsStream("/Slime01/Stand12.png"));

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
