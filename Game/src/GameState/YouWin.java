/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameState;

import TileMap.Background;
import com.sun.glass.events.KeyEvent;
import game.GamePanel;
import java.awt.Color;
import java.awt.Graphics2D;


public class YouWin extends GameState {
    
    public boolean exit;
    public boolean playagain;
    public Background bg;
    
    public YouWin(GameStateManager gsm){
        exit =false;
        playagain= false;
        this.gsm=gsm;
        init();
    }

    @Override
    public void init() {    
        exit =false;
        bg = new Background("/texture/youWin.png",0.1);
    }

    @Override
    public void draw(Graphics2D g) {
        
        //clear screen
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, GamePanel.WIDTH,GamePanel.HEIGHT);
       
        //draw bg
        bg.draw(g);
      
    }

   

    @Override
    public void update() {
        
        if(exit) gsm.setState(0);
        if(playagain) gsm.setState(1);
    }

    @Override
    public void keyPressed(int k) {
        
        if(k== KeyEvent.VK_ESCAPE) exit = true;
        if(k== KeyEvent.VK_ENTER) playagain = true;
        
    }

    @Override
    public void keyReleased(int k) {
    if(k== KeyEvent.VK_ESCAPE) exit = false;
    if(k== KeyEvent.VK_ENTER) playagain = true;

   
    }
    
}
