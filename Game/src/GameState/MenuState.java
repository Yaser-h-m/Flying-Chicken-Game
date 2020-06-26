/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameState;

import java.awt.*;
import TileMap.Background;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

public class MenuState extends GameState {

    private Background bg;
    private int currentChoice=0;
    private String[] options={
        "Start",
        "Help",
        "Quit"  
    };
    
    private Color titleColor;
    private Font titleFont,font;
    
    public MenuState(GameStateManager gsm)
    {
        this.gsm= gsm;
        
        try{
            bg =new Background("/texture/WelcomPage1.png",2);
            bg.setVector(0, 0);
            
            titleColor = new Color(128,0,0);
            titleFont = new Font("Century Gothic", Font.PLAIN,28);
            font= new Font("Arial",Font.PLAIN,28);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
    }
    @Override
    public void init() {
 this.gsm= gsm;
        
        try{
            bg =new Background("/texture/WelcomPage1.png",2);
            bg.setVector(0, 0);
            
            titleColor = new Color(128,0,0);
            titleFont = new Font("Century Gothic", Font.PLAIN,28);
            font= new Font("Arial",Font.PLAIN,28);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void update() {
        bg.update();
    
    }

    @Override
    public void draw(Graphics2D g) {
        bg.draw(g);
        
        //draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
       // g.drawString("Flying Chicken", 230, 70);
        
        //draw menu options
        g.setFont(font);
        
        for(int i=0; i<options.length; i++)
        {
            if(i==currentChoice)
            {
                g.setColor(Color.RED);
            }
            else
            {
                g.setColor(Color.BLACK);
            }
            g.drawString(options[i],200 ,360+i*28 );
        }
        
                
    }

    private void select()
    {
        if(currentChoice==0)
        {
            gsm.setState(GameStateManager.LEVEL1STATE);
        }
        
        if(currentChoice==1)
        {
            //help
        }
        if(currentChoice==2)
        {
          System.exit(0);
        }
    }
    
    @Override
    public void keyPressed(int k) {
        if(k== KeyEvent.VK_ENTER)
        {
            select();
        }
        
        if(k== KeyEvent.VK_UP)
        {
            currentChoice--;
            if(currentChoice==-1)
            {
                currentChoice=options.length-1;
            }
            
        }
        
        if(k== KeyEvent.VK_DOWN)
        {
            currentChoice++;
            if(currentChoice==options.length)
            {
                currentChoice=0;
            }
        }
    }

    @Override
    public void keyReleased(int k) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
