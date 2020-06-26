/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class HUD {
    
    private Player player;
    private BufferedImage image;
    private Font font;
    public HUD(Player p){
        player =p;
        
        try{
            image =ImageIO.read(
                    getClass().getResourceAsStream(
                    "/Player/hud.gif"));
            font = new Font("Arial", Font.PLAIN, 14);
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
 
    public void draw(Graphics2D g){
        g.drawImage(image,0 , 10, null);
        g.setFont(font);
        g.drawString(player.getHealth()+"/"+player.getMaxHealth(), 30, 25);
        g.drawString(player.score+" S",30, 45);
     
    }
}
